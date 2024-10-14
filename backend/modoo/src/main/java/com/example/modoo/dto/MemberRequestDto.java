package com.example.modoo.dto;

import com.example.modoo.entity.Authority;
import com.example.modoo.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * MemberRequestDto는 회원 등록 요청을 처리할 때 사용되는 데이터 전송 객체(DTO)입니다.
 * 이 클래스는 클라이언트 측에서 서버로 사용자 데이터(이메일 및 비밀번호)를 전달하는 데 사용됩니다.
 * 또한, 제공된 PasswordEncoder를 사용하여 이 DTO를 Member 엔티티로 변환하는 메소드를 제공합니다.
 * 이 과정에서 비밀번호 암호화가 처리되어 민감한 데이터의 보안이 유지됩니다.
 *
 * 주요 책임:
 * - 클라이언트에서 서버로 사용자 등록 정보를 캡처하고 전달합니다.
 * - 자체를 도메인 모델(Member 엔티티)로 변환하는 메소드를 제공하여 비밀번호를 암호화하고 기본 권한을 설정합니다.
 * - 이 변환은 Member 엔티티를 안전하게 인스턴스화하기 위해 필요한 비즈니스 로직을 캡슐화합니다.
 *
 * 사용 방법:
 * - 이 DTO는 회원가입 과정에서 사용자가 제공한 이메일과 비밀번호로 인스턴스화됩니다.
 * - 그 후 서비스 계층으로 전달되어 `toMember(PasswordEncoder)` 메소드를 사용하여 해시된 비밀번호를 포함하는 Member 엔티티를 생성하며,
 *   이는 데이터베이스에 저장될 수 있습니다.
 */

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDto {
    private String email;
    private String password;
    private String fullName;
    private String studentId;


    // 모든 필드를 받는 생성자
    public MemberRequestDto(String email, String password, String fullName, String studentId) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.studentId = studentId;
    }

    // 이메일과 비밀번호만 받는 생성자
    public MemberRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // DTO를 Member 엔티티로 변환하는 메소드
    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // 비밀번호 암호화
                .fullName(fullName)
                .studentId(studentId)
                .authority(Authority.ROLE_USER) // 예시로 ROLE_USER를 사용했습니다.
                .build();
    }

    // 사용자의 로그인 시도에 필요한 인증 토큰을 생성
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
