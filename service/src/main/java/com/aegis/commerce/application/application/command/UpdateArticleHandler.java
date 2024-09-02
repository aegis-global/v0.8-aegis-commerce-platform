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
import com.aegis.commerce.application.api.command.UpdateArticle;
import com.aegis.commerce.application.api.command.UpdateArticleResult;
import com.aegis.commerce.application.api.dto.UpdateArticleDto;
import com.aegis.commerce.application.application.ArticleAssembler;
import com.aegis.commerce.application.application.service.SlugService;
import com.aegis.commerce.application.domain.model.Article;
import com.aegis.commerce.application.domain.model.User;
import com.aegis.commerce.application.domain.repository.ArticleRepository;
import com.aegis.commerce.application.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.aegis.commerce.application.application.exception.Exceptions.badRequest;
import static com.aegis.commerce.application.application.exception.Exceptions.forbidden;
import static com.aegis.commerce.application.application.exception.Exceptions.notFound;

@RequiredArgsConstructor
@Singleton
public class UpdateArticleHandler implements CommandHandler<UpdateArticleResult, UpdateArticle> {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final SlugService slugService;

    @Transactional
    @Override
    public UpdateArticleResult handle(UpdateArticle command) {
        Article article = articleRepository.findBySlug(command.getSlug())
                .orElseThrow(() -> notFound("article [slug=%s] does not exist", command.getSlug()));

        if (!Objects.equals(article.getAuthor().getUsername(), command.getCurrentUsername())) {
            throw forbidden("article [slug=%s] is not owned by %s", command.getSlug(), command.getCurrentUsername());
        }

        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        UpdateArticleDto data = command.getArticle();
        Article alteredArticle = article.toBuilder()
                .slug(data.getTitle() != null ? slugService.makeSlug(data.getTitle()) : article.getSlug())
                .title(data.getTitle() != null ? data.getTitle() : article.getTitle())
                .description(data.getDescription() != null ? data.getDescription() : article.getDescription())
                .body(data.getBody() != null ? data.getBody() : article.getBody())
                .updatedAt(ZonedDateTime.now())
                .build();

        articleRepository.save(alteredArticle);

        return new UpdateArticleResult(ArticleAssembler.assemble(alteredArticle, currentUser));
    }

}
