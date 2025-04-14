package kz.bitlab.mainservice.service;


import kz.bitlab.mainservice.dto.ChapterRequest;
import kz.bitlab.mainservice.dto.ChapterResponse;

import java.util.List;

public interface ChapterService {

    List<ChapterResponse> getChapter();

    ChapterResponse getChapterById(Long id);

    void createChapter(ChapterRequest request);

    void deleteChapterById(Long id);
}
