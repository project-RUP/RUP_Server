package com.rup.repository;

import com.rup.domain.mapping.PromiseMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromiseMemberRepository extends JpaRepository<PromiseMember, Long> {
    List<PromiseMember> findAllByMemberId(Long memberId);

    List<PromiseMember> findAllByPromiseId(Long memberId);

    Optional<PromiseMember> findByMemberIdAndPromiseId(Long memberId, Long promiseId);
}
