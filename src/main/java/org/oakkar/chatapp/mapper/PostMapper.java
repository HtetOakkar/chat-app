package org.oakkar.chatapp.mapper;

import org.oakkar.chatapp.model.dto.PostDto;
import org.oakkar.chatapp.model.entity.Post;
import org.oakkar.chatapp.model.record.PostRecord;

public interface PostMapper {
    PostRecord mapToRecord(Post savedPost);

    PostDto mapToDto(Post savedPost);

    PostRecord maptoRecord(PostDto postDto);
}
