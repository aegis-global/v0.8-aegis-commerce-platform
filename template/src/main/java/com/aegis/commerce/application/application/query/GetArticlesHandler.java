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

import com.aegis.commerce.bus.QueryHandler;
import com.aegis.commerce.application.api.dto.ArticleDto;
import com.aegis.commerce.application.api.query.GetArticles;
import com.aegis.commerce.application.api.query.GetArticlesResult;
import com.aegis.commerce.application.application.ArticleAssembler;
import com.aegis.commerce.application.domain.model.Article;
import com.aegis.commerce.application.domain.model.User;
import com.aegis.commerce.application.domain.repository.ArticleRepository;
import com.aegis.commerce.application.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Singleton
public class GetArticlesHandler implements QueryHandler<GetArticlesResult, GetArticles> {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public GetArticlesResult handle(GetArticles query) {
        List<Article> articles = articleRepository
                .findByFilters(query.getTag(), query.getAuthor(), query.getFavorited(), query.getLimit(), query.getOffset());

        User currentUser = userRepository.findByUsername(query.getCurrentUsername())
                .orElse(null);

        List<ArticleDto> results = new ArrayList<>();

        articles.forEach(article -> results.add(ArticleAssembler.assemble(article, currentUser)));

        return new GetArticlesResult(results, results.size());
    }

}
