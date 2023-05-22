package com.example.youcandoit.member.controller;

import com.example.youcandoit.member.dto.MemberDto;
import com.example.youcandoit.member.service.MemberService;
import com.example.youcandoit.member.service.SnsLoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.time.LocalDate;

@RestController
@RequestMapping("api/member-api")
public class MemberController {

    private MemberService memberService;
    private SnsLoginService snsLoginService;

    @Autowired
    public MemberController(MemberService memberService, SnsLoginService snsLoginService) {
        this.memberService = memberService;
        this.snsLoginService = snsLoginService;
    }

    // api/member-api/login
    // 로그인
    @PostMapping("/login")
    public String loginMember(@RequestBody MemberDto mDto, HttpSession session) {
        MemberDto memberDto = memberService.loginMember(mDto);

        if(memberDto == null)
            return null;
        session.setAttribute("loginId", memberDto.getMemId());
        return memberDto.getNickname();
    }

    // api/member-api/duplicate-id/{id}
    // 아이디 중복 확인
    @GetMapping("/duplicate-id/{memId}")
    public boolean duplicate(@PathVariable String memId) {
        MemberDto memberDto = memberService.getId(memId);

        if(memberDto == null)
            return false;
        return true;
    }

    // api/member-api/signup
    // 회원가입
    @PostMapping("/signup")
    public void createMember(@RequestBody MemberDto memberDto) {
        memberDto.setJoinDate(String.valueOf(LocalDate.now()));
        memberService.saveMember(memberDto);
    }

    // api/member-api/insert-profile
    // 프로필사진 업로드
    @PostMapping("/insert-profile")
    public void insertProfile(@RequestParam String memId, @RequestPart MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.length()-4); // 확장자 추출
        String path = "D:/project/profile/"; // 저장 경로 지정
        String safeName = path + memId + "Profile" + extension; // 저장명 지정

        try {
            file.transferTo(new File(safeName)); // 파일 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
        MemberDto memberDto = MemberDto.builder()
                .memId(memId)
                .profilePicture(safeName)
                .build();
        memberService.saveProfile(memberDto);
    }

    // 전화번호로 아이디 찾기
    @PostMapping("/find-id")
    public MemberDto findId(@RequestBody MemberDto mDto) {
        MemberDto memberDto = memberService.findId(mDto);

        if(memberDto == null)
            return null;
        return memberDto;
    }

    // 아이디와 전화번호가 일치하는 회원이 있는지 확인
    @PostMapping("/find-pw")
    public MemberDto findPw(@RequestBody MemberDto mDto) {
        MemberDto memberDto = memberService.findPw(mDto);

        if(memberDto == null)
            return null;
        return memberDto;
    }

    // 비밀번호 재설정
    @PostMapping("/pw-reset")
    public void pwReset(@RequestBody MemberDto memberDto) {
        memberService.resetPw(memberDto);
    }

    // 로그인 여부
    @GetMapping("is-login")
    public String isLogin(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        if(loginId == null) {
            return null;
        }
        MemberDto memberDto = memberService.getId(loginId);
        return memberDto.getNickname();
    }

    // 로그아웃
    @GetMapping("logout")
    public void logout(HttpSession session) {
        session.removeAttribute("loginId");
    }

    // 인증번호 발송
    @PostMapping("sms-send")
    public boolean smsSend(@RequestBody MemberDto mDto, HttpSession session) {
        // 인증번호 생성
        Random random = new Random();
        int createNum;
        String certifyNum = "";
        for(int i=0; i<6; i++) {
            createNum = random.nextInt(9);
            certifyNum += Integer.toString(createNum);
        }

        try{ // sms 인증번호 보내기
            String charsetType = "UTF-8";
            String sms_url = "https://sslsms.cafe24.com/sms_sender.php"; // SMS 전송요청 URL
            String nointeractive = "";

            String[] host_info = sms_url.split("/");
            String host = host_info[2];
            String path = "/" + host_info[3];
            int port = 80;

            String arrKey[]
                    = new String[] {"user_id","secure","msg", "rphone","sphone1","sphone2","sphone3","rdate","rtime"
                    ,"mode","testflag","destination","repeatFlag","repeatNum", "repeatTime", "smsType", "subject"};
            String valKey[]= new String[arrKey.length] ;
            valKey[0] = "ycdisms"; // user_id
            valKey[1] = "6670b493a259ac20472cecf836d600e6"; // secure
            valKey[2] = "[유캔두잇] 인증번호 [" + certifyNum + "]를 입력해주세요."; // msg
            valKey[3] = mDto.getPhoneNumber(); // rphone
            valKey[4] = "010"; // sphone1
            valKey[5] = "7612"; // sphone2
            valKey[6] = "5780"; // sphone3
            valKey[7] = ""; // rdate
            valKey[8] = ""; // rtime
            valKey[9] = "1"; // mode
            valKey[10] = ""; // testflag
            valKey[11] = ""; // destination
            valKey[12] = ""; // repeatFlag
            valKey[13] = ""; // repeatNum;
            valKey[14] = ""; // repeatTime;
            valKey[15] = "S"; // smsType;
            valKey[16] = ""; // subject;

            String boundary = "";
            Random rnd = new Random();
            String rndKey = Integer.toString(rnd.nextInt(32000));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytData = rndKey.getBytes();
            md.update(bytData);
            byte[] digest = md.digest();
            for(int i =0;i<digest.length;i++)
            {
                boundary = boundary + Integer.toHexString(digest[i] & 0xFF);
            }
            boundary = "---------------------"+boundary.substring(0,11);

            // 본문 생성
            String data = "";
            String index = "";
            String value = "";
            for (int i=0;i<arrKey.length; i++)
            {
                index =  arrKey[i];
                value = valKey[i];
                data +="--"+boundary+"\r\n";
                data += "Content-Disposition: form-data; name=\""+index+"\"\r\n";
                data += "\r\n"+value+"\r\n";
                data +="--"+boundary+"\r\n";
            }

            //out.println(data);

            InetAddress addr = InetAddress.getByName(host);
            Socket socket = new Socket(host, port);
            // 헤더 전송
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charsetType));
            wr.write("POST "+path+" HTTP/1.0\r\n");
            wr.write("Content-Length: "+data.length()+"\r\n");
            wr.write("Content-type: multipart/form-data, boundary="+boundary+"\r\n");
            wr.write("\r\n");

            // 데이터 전송
            wr.write(data);
            wr.flush();

            // 결과값 얻기
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(),charsetType));
            String line;
            String alert = "";
            ArrayList tmpArr = new ArrayList();
            while ((line = rd.readLine()) != null) {
                tmpArr.add(line);
            }
            wr.close();
            rd.close();

            String tmpMsg = (String)tmpArr.get(tmpArr.size()-1);
            String[] rMsg = tmpMsg.split(",");
            String Result= rMsg[0]; //발송결과
            String Count= ""; //잔여건수
            if(rMsg.length>1) {Count= rMsg[1]; }

            //발송결과 알림
            if(Result.equals("success")) {
                System.out.println("Count : " + Count);
                session.setAttribute("phoneNumber", mDto.getPhoneNumber());
                session.setAttribute("certifyNumber", certifyNum);
                return true;
            }
            else if(Result.equals("reserved")) {
                System.out.println("Count : " + Count);
                return true;
            }
            else if(Result.equals("3205")) {
                System.out.println("not number"); // 번호형식이 잘못됨
                return false;
            }
            else {
                System.out.println("[Error]" + Result);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 인증하기
    @PostMapping("certify")
    public boolean certify(@RequestParam String phoneNumber, @RequestParam String certifyNum, HttpSession session) {
        if(phoneNumber.equals((String)session.getAttribute("phoneNumber"))
                && certifyNum.equals((String)session.getAttribute("certifyNumber"))) { // 인증번호와 전화번호가 일치하면
            session.invalidate(); // 세션 삭제
            return true;
        } else {
            return false;
        }
    }

    // sns 로그인
    @GetMapping("oauth2/code/{registrationId}")
    public Object snsLogin(@RequestParam String code, @PathVariable String registrationId, HttpSession session) {
        String accessToken = snsLoginService.getAccessToken(code, registrationId);
        HashMap<String, Object> userInfo = snsLoginService.getResource(accessToken, registrationId);
        MemberDto mDto = MemberDto.builder()
                .memId(userInfo.get("email").toString())
                .password(userInfo.get("id").toString())
                .memClass("2")
                .build();
        MemberDto memberDto = memberService.loginMember(mDto);

        if(memberDto == null)
            return userInfo;
        session.setAttribute("loginId", memberDto.getMemId());
        return memberDto.getNickname();
    }
}
