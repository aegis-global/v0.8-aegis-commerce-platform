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
import com.aegis.commerce.application.domain.repository.MembershipRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Singleton
public class JpaMembershipRepositoryAdapter implements MembershipRepository {

    private final DataMembershipRepository repository;

    @Override
    public Optional<Membership> findBySlug(String slug) {
        return repository.findBySlug(slug);
    }


    @Override
    public List<Membership> findByMembers(List<UUID> members, Integer limit, Integer offset) {
        OffsetBasedPageable pageable = new OffsetBasedPageable(limit, offset, Sort.of(Sort.Order.desc("createdAt")));
        return repository.findByMembers(members, pageable);
    }

    @Override
    public void delete(Membership membership) {
        repository.delete(membership);
    }

    @Override
    public Membership save(Membership membership) {
        if (repository.existsById(membership.getId())) {
            return repository.update(membership);
        }
        return repository.save(membership);
    }

    @RequiredArgsConstructor
    @Getter
    private static class OffsetBasedPageable implements Pageable {
        private final int size;
        private int number;
        private final long offset;
        private final Sort sort;
    }

}
