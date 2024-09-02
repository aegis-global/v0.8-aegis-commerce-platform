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
package com.aegis.commerce.application.application.command;

import com.aegis.commerce.application.api.command.DeleteOrganization;
import com.aegis.commerce.application.api.command.DeleteOrganizationResult;
import com.aegis.commerce.bus.CommandHandler;
import com.aegis.commerce.application.domain.model.Organization;
import com.aegis.commerce.application.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.Objects;

import static com.aegis.commerce.application.application.exception.Exceptions.forbidden;
import static com.aegis.commerce.application.application.exception.Exceptions.notFound;

@RequiredArgsConstructor
@Singleton
public class DeleteOrganizationHandler implements CommandHandler<DeleteOrganizationResult, DeleteOrganization> {

    private final OrganizationRepository organizationRepository;

    @Transactional
    @Override
    public DeleteOrganizationResult handle(DeleteOrganization command) {
        Organization organization = organizationRepository.findBySlug(command.getSlug())
                .orElseThrow(() -> notFound("organization [slug=%s] does not exist", command.getSlug()));

        if (!Objects.equals(organization.getOwner().getUsername(), command.getCurrentUsername())) {
            throw forbidden("organization [slug=%s] is not owned by %s", command.getSlug(), command.getCurrentUsername());
        }

        organizationRepository.delete(organization);

        return new DeleteOrganizationResult();
    }

}
