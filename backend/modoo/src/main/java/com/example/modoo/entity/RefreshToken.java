package com.example.modoo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RefreshToken 클래스는 사용자의 리프레시 토큰을 데이터베이스에 저장하기 위한 엔티티입니다.
 * 이 클래스는 리프레시 토큰과 관련된 주요 정보를 저장하며, 각 토큰은 고유한 사용자(또는 멤버) ID에 연결됩니다.
 *
 * 주요 필드:
 * - key: 데이터베이스에서 리프레시 토큰을 유일하게 식별하는 데 사용되는 필드로, 사용자의 ID 값을 저장합니다.
 * - value: 리프레시 토큰의 실제 문자열 값입니다. 이 토큰은 사용자가 새로운 액세스 토큰을 요청할 때 사용됩니다.
 *
 * 기능:
 * - updateValue(String token): 기존의 리프레시 토큰 값을 새로운 값으로 업데이트하는 메서드입니다.
 *   이 메서드는 새로운 리프레시 토큰을 받아 기존의 값과 교체하고, 업데이트된 엔티티를 반환합니다.
 *
 * 사용 방법:
 * - 리프레시 토큰은 사용자의 세션이 만료된 후에도 로그인 상태를 유지하기 위해 사용됩니다.
 * - 사용자가 로그인을 요청할 때, 시스템은 액세스 토큰과 함께 리프레시 토큰도 발급하고, 이 토큰은 본 클래스의 인스턴스로 저장됩니다.
 * - 액세스 토큰이 만료되었을 때, 사용자는 이 리프레시 토큰을 사용하여 새 액세스 토큰을 요청할 수 있습니다.
 */

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {
    @Id
    @Column(name = "rt_key")
    private String key;

    @Column(name = "rt_value")
    private String value;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
