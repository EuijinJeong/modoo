package com.example.modoo.service;

import com.example.modoo.entity.Member;
import com.example.modoo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordResetService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 비밀번호 초기화 메서드
     *
     * @param email
     */
    public void resetPassword(String email) {
        // 1. 이메일로 사용자 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));

        // 2. 임시 비밀번호 생성
        String tempPassword = generateTemporaryPassword(10); // 10자리 비밀번호 생성

        // 3. 임시 비밀번호를 암호화하고 저장
        updateMemberPassword(member, tempPassword);

        // 4. 사용자에게 임시 비밀번호 이메일 전송
        sendTempPasswordEmail(member.getEmail(), tempPassword);
    }

    /**
     * 비밀번호 업데이트 메서드
     *
     * @param email
     * @param newPassword
     */
    public void updatePassword(String email, String newPassword) {
        // 1. 이메일로 사용자 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));

        // 2. 새 비밀번호를 암호화하고 저장
        updateMemberPassword(member, newPassword);
    }

    /**
     * 비밀번호를 암호화하고 저장하는 메서드
     *
     * @param member
     * @param rawPassword
     */
    private void updateMemberPassword(Member member, String rawPassword) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);
        // DB에 암호화된 비밀번호 저장
        member.setPassword(encodedPassword);
        memberRepository.save(member);
    }

    /**
     * 임시 비밀번호 생성 메서드
     *
     * @param length
     * @return
     */
    private String generateTemporaryPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

    /**
     * 임시 비밀번호 사용자에게 전송하는 메서드
     *
     * @param email
     * @param tempPassword
     */
    private void sendTempPasswordEmail(String email, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[모두의 전공책] 임시 비밀번호 안내");
        message.setText("임시 비밀번호는 다음과 같습니다: " + tempPassword);

        mailSender.send(message);
    }
}