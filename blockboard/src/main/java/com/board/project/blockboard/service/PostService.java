package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import com.board.project.blockboard.mapper.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PostService{
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private BoardMapper boardMapper;

    public void insertPost(PostDTO post) {
        postMapper.insertPost(post);

    }
    public void deletePost(int postID) {
        postMapper.deletePostByPostID(postID);
    }
    public PostDTO selectPostByPostID(int postID) {
        return postMapper.selectPostByPostID(postID);
    }
    public void updatePost(PostDTO post) {
        postMapper.updatePost(post);
    }
    public JSONArray searchPost(String option, String keyword) {
        List<PostDTO> result =  postMapper.search(option,keyword);;
        JSONArray resultToJSON = new JSONArray();
        for(PostDTO post : result) {
            JSONObject object = new JSONObject();
            object.put("postTitle", post.getPostTitle());
            object.put("postID", post.getPostID());
            object.put("companyID", post.getCompanyID());
            object.put("boardID", post.getBoardID());
            object.put("postContent", post.getPostContent());
            object.put("userName", post.getUserName());
            object.put("userID", post.getUserID());
            object.put("postRegisterTime", post.getPostRegisterTime());
            object.put("boardName",post.getBoardName());
            resultToJSON.add(object);
        }
        return resultToJSON;
    }
}
