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
package com.aegis.commerce.application.application.query;

import com.aegis.commerce.application.api.query.GetMember;
import com.aegis.commerce.application.api.query.GetMemberResult;
import com.aegis.commerce.application.application.MemberAssembler;
import com.aegis.commerce.bus.QueryHandler;
import com.aegis.commerce.application.domain.model.Organization;
import com.aegis.commerce.application.domain.model.Member;
import com.aegis.commerce.application.domain.model.User;
import com.aegis.commerce.application.domain.repository.OrganizationRepository;
import com.aegis.commerce.application.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import static com.aegis.commerce.application.application.exception.Exceptions.notFound;

@RequiredArgsConstructor
@Singleton
public class GetMemberHandler implements QueryHandler<GetMemberResult, GetMember> {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public GetMemberResult handle(GetMember query) {
        Organization organization = organizationRepository.findBySlug(query.getSlug())
                .orElseThrow(() -> notFound("organization [slug=%s] does not exists", query.getSlug()));

        User currentUser = userRepository.findByUsername(query.getCurrentUsername())
                .orElse(null);

        Member member = organization.getMembers().stream()
                .filter(c -> c.getId().equals(query.getId()))
                .findFirst()
                .orElseThrow(() -> notFound("member [id=%s] does not exists", query.getId()));

        return new GetMemberResult(MemberAssembler.assemble(member));
    }

}
