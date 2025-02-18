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
import com.aegis.commerce.application.api.command.CreateArticle;
import com.aegis.commerce.application.api.command.CreateArticleResult;
import com.aegis.commerce.application.api.dto.CreateArticleDto;
import com.aegis.commerce.application.application.ArticleAssembler;
import com.aegis.commerce.application.application.service.SlugService;
import com.aegis.commerce.application.domain.model.Article;
import com.aegis.commerce.application.domain.model.Tag;
import com.aegis.commerce.application.domain.model.User;
import com.aegis.commerce.application.domain.repository.ArticleRepository;
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
public class CreateArticleHandler implements CommandHandler<CreateArticleResult, CreateArticle> {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final SlugService slugService;

    @Transactional
    @Override
    public CreateArticleResult handle(CreateArticle command) {
        CreateArticleDto data = command.getArticle();
        Optional<Article> articleByTitleOptional = articleRepository.findByTitle(data.getTitle());
        if (articleByTitleOptional.isPresent()) {
            throw badRequest("article [title=%s] already exists", data.getTitle());
        }

        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        ZonedDateTime now = ZonedDateTime.now();

        Article.ArticleBuilder articleBuilder = Article.builder()
                .id(UUID.randomUUID())
                .slug(slugService.makeSlug(data.getTitle()))
                .title(data.getTitle())
                .description(data.getDescription())
                .body(data.getBody())
                .createdAt(now)
                .updatedAt(now)
                .author(currentUser);

        if (data.getTagList() != null) {
            data.getTagList().stream()
                    .map(t -> tagRepository.findByName(t).orElseGet(() -> new Tag(t)))
                    .forEach(articleBuilder::tag);
        }

        Article savedArticle = articleRepository.save(articleBuilder.build());

        return new CreateArticleResult(ArticleAssembler.assemble(savedArticle, currentUser));
    }

}
