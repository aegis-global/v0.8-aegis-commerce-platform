/*
 * MIT License
 *
 * Copyright (c) 2020 - present Aegis GIS, LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.aegis.commerce.application.infrastructure.db.jpa;

import com.aegis.commerce.application.domain.model.Membership;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import com.aegis.commerce.application.domain.repository.MembershipRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataMembershipRepository extends CrudRepository<Membership, UUID> {

    Optional<Membership> findBySlug(String slug);

    Optional<Membership> findByName(String name);

    @Query("SELECT DISTINCT membership_ FROM Membership membership_ " +
            "LEFT JOIN membership_.tags t " +
            "WHERE " +
            "(:tag IS NULL OR t.name = :tag)")
    List<Membership> findByFilters(@Nullable String tag,
                                @Nullable String favorited,
                                Pageable pageable);

    @Query("SELECT membership_ FROM Membership membership_ JOIN membership_.member member WHERE member.id IN :members")
    List<Membership> findByMembers(List<UUID> members, Pageable pageable);

}
