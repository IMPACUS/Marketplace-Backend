package com.impacus.maketplace.service.review;

import com.impacus.maketplace.repository.review.ReviewReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewReplyService {
    private final ReviewReplyRepository reviewReplyRepository;
}
