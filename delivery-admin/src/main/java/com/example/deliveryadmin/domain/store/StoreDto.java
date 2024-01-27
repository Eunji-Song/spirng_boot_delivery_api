package com.example.deliveryadmin.domain.store;

import com.example.deliveryadmin.common.embeded.Address;
import com.example.deliveryadmin.common.embeded.OpeningHours;
import com.example.deliveryadmin.common.enums.StoreCategory;
import com.example.deliveryadmin.common.enums.StoreStatus;
import com.example.deliveryadmin.common.fileupload.dto.AttachmentFileDto;
import com.example.deliveryadmin.common.fileupload.dto.StoreAttachmentFileDto;
import com.example.deliveryadmin.domain.member.Member;
import com.example.deliveryadmin.domain.member.MemberDto;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class StoreDto {
    // === Request(역직렬화) === //

    /**
     * 전체 목록 조회
     * 필터링 대상 : 카테고리, 운영 상태, 매장명
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RequestSearchDto {
        private StoreCategory category;
        private StoreStatus status;
        private String name;

        public RequestSearchDto(StoreCategory category, StoreStatus status, String name) {
            this.category = category;
            this.status = status;
            this.name = name;
        }
    }


    /**
     * 신규 매장 등록
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RequestSaveDto {
        @NotBlank(message = "매장명을 입력해주세요.")
        @Size(max = 20)
        private String name;

        private MemberDto.DetailInfo member;

        private String description;

        @NotNull(message = "주소는 필수 입니다.")
        private Address address;

        @NotNull(message = "매장 운영 시간을 입력해주세요.")
        private OpeningHours openingHours;

        @NotNull(message = "매장 카테고리를 선택해주세요.")
        private StoreCategory category;

        @NotNull(message = "매장 운영 상태를 선택해주세요.")
        private StoreStatus status;

        @Builder
        public RequestSaveDto(String name, MemberDto.DetailInfo member, String description, Address address, OpeningHours openingHours, StoreCategory category, StoreStatus status) {
            this.name = name;
            this.member = member;
            this.description = description;
            this.address = address;
            this.openingHours = openingHours;
            this.category = category;
            this.status = status;
        }


        // StoreService - save DTO에 사용자 정보 할당 할 때 사용
        public void setMember(Member member) {
            this.member = new MemberDto.DetailInfo(member);
        }

    }

    /**
     * 매장 정보 수정
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class RequestUpdateDto {
        @NotBlank(message = "매장명을 입력해주세요.")
        @Size(max = 20)
        private String name;

        private String description;

        @NotNull(message = "주소는 필수 입니다.")
        private Address address;

        @NotNull(message = "매장 운영 시간을 입력해주세요.")
        private OpeningHours openingHours;

        @NotNull(message = "매장 운영 상태를 선택해주세요.")
        private StoreStatus status;

        @Builder(toBuilder = true)
        public RequestUpdateDto(String name, String description, Address address, OpeningHours openingHours, StoreStatus status) {
            this.name = name;
            this.description = description;
            this.address = address;
            this.openingHours = openingHours;
            this.status = status;
        }


    }


    // === Response(직렬화) === //

    @Getter
    @ToString
    public static class DetailInfo {
        private Long id;

        private String name;

        private String description;

        private Address address;

        private OpeningHours openingHours;

        private StoreCategory category;

        private StoreStatus status;

        private MemberDto.DetailInfo member;

        private StoreAttachmentFileDto.Thumbnail thumbnail;


        private List<StoreAttachmentFileDto.DetailImages> detailImages = new ArrayList<>();


        public DetailInfo() {
        }

        @QueryProjection
        public DetailInfo(Store store, Member member) {
            // store
            this.id = store.getId();
            this.name = store.getName();
            this.description = store.getDescription();
            this.address = store.getAddress();
            this.openingHours = store.getOpeningHours();
            this.category = store.getCategory();
            this.status = store.getStatus();

            // member
            this.member = new MemberDto.DetailInfo(member);

            // 썸네일
            this.thumbnail = null;

            // 상세 이미지
            this.detailImages = new ArrayList<>();
        }

        public DetailInfo(StoreDto.DetailInfo detailInfo, StoreAttachmentFileDto.Thumbnail thumbnail, List<StoreAttachmentFileDto.DetailImages> detailImages) {
            // store
            this.id = detailInfo.getId();
            this.name = detailInfo.getName();
            this.description = detailInfo.getDescription();
            this.address = detailInfo.getAddress();
            this.openingHours = detailInfo.getOpeningHours();
            this.category = detailInfo.getCategory();
            this.status = detailInfo.getStatus();

            // member
            this.member = detailInfo.getMember();

            this.thumbnail = thumbnail;
            this.detailImages = detailImages;

        }
    }

    @Getter
    @ToString
    public static class ListViewData {
        private Long id;

        private String name;

        private Address address;

        private StoreCategory category;

        private StoreStatus status;

        private StoreAttachmentFileDto.Thumbnail thumbnail;

        public ListViewData() {
        }
    }
}
