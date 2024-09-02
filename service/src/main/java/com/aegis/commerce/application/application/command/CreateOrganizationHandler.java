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

import com.aegis.commerce.bus.CommandHandler;
import com.aegis.commerce.application.api.command.CreateOrganization;
import com.aegis.commerce.application.api.command.CreateOrganizationResult;
import com.aegis.commerce.application.api.dto.CreateOrganizationDto;
import com.aegis.commerce.application.application.OrganizationAssembler;
import com.aegis.commerce.application.application.service.SlugService;
import com.aegis.commerce.application.domain.model.Organization;
import com.aegis.commerce.application.domain.model.Tag;
import com.aegis.commerce.application.domain.model.User;
import com.aegis.commerce.application.domain.repository.OrganizationRepository;
import com.aegis.commerce.application.domain.repository.TagRepository;
import com.aegis.commerce.application.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.aegis.commerce.application.application.exception.Exceptions.badRequest;

@RequiredArgsConstructor
@Singleton
public class CreateOrganizationHandler implements CommandHandler<CreateOrganizationResult, CreateOrganization> {

    private final OrganizationRepository organizationRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final SlugService slugService;

    @Transactional
    @Override
    public CreateOrganizationResult handle(CreateOrganization command) {
        CreateOrganizationDto data = command.getOrganization();
        Optional<Organization> organizationByNameOptional = organizationRepository.findByName(data.getName());
        if (organizationByNameOptional.isPresent()) {
            throw badRequest("organization [name=%s] already exists", data.getName());
        }

        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        ZonedDateTime now = ZonedDateTime.now();

        Organization.OrganizationBuilder organizationBuilder = Organization.builder()
                .id(UUID.randomUUID())
                .slug(slugService.makeSlug(data.getName()))
                .name(data.getName())
                .description(data.getDescription())
                .content(data.getContent())
                .createdAt(now)
                .updatedAt(now)
                .owner(currentUser);

        if (data.getTagList() != null) {
            data.getTagList().stream()
                    .map(t -> tagRepository.findByName(t).orElseGet(() -> new Tag(t)))
                    .forEach(organizationBuilder::tag);
        }

        Organization savedOrganization = organizationRepository.save(organizationBuilder.build());

        return new CreateOrganizationResult(OrganizationAssembler.assemble(savedOrganization, currentUser));
    }

}
