package com.example.modoo.service;

import com.example.modoo.dto.AddressDto;
import com.example.modoo.entity.Member;
import com.example.modoo.entity.MemberAddress;
import com.example.modoo.repository.MemberAddressRepository;
import com.example.modoo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private MemberAddressRepository memberAddressRepository;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 사용자의 기본 주소를 조회하는 메소드
     * @param memberId
     * @return 사용자의 기본 주소
     */
    public MemberAddress getDefaultMemberAddress(Long memberId) {
        return memberAddressRepository.findByMemberIdAndIsDefault(memberId, true);
    }


    /**
     * 배송지 정보를 저장하는 메소드
     *
     * @param memberId
     * @param addressDto
     * @return
     */
    public MemberAddress saveMemberAddress(Long memberId, AddressDto addressDto) {

        // 사용자 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + memberId));

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setRecipient(addressDto.getRecipient());
        memberAddress.setPhone(addressDto.getPhone());
        memberAddress.setAddressLine1(addressDto.getAddressLine1());
        memberAddress.setAddressLine2(addressDto.getAddressLine2());
        memberAddress.setPostalCode(addressDto.getPostalCode());
        memberAddress.setMember(member);

        return memberAddressRepository.save(memberAddress);
    }

    public MemberAddress updateMemberAddress(Long addressId, AddressDto addressDto) {

        // 주소 아이디 조회
        MemberAddress memberAddress = memberAddressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("주소정보를 찾을 수 없습니다:" + addressId));

        // Dto를 활용해서 엔티티 정보 업데이트
        memberAddress.setRecipient(addressDto.getRecipient());
        memberAddress.setPhone(addressDto.getPhone());
        memberAddress.setAddressLine1(addressDto.getAddressLine1());
        memberAddress.setAddressLine2(addressDto.getAddressLine2());
        memberAddress.setPostalCode(addressDto.getPostalCode());

        // 업데이트된 주소 정보 저장
        return memberAddressRepository.save(memberAddress);
    }
}