package com.example.forum.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {

    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;

    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPagination(Integer totalCount, Integer page, Integer size) {

        totalPage = totalCount%size==0?totalCount/size:totalCount/size+1;
        pages.add(page);

        for(int i=1;i<4;i++){
            if(page - i > 0){
                pages.add(0,page -i);
            }
            if(page + i <=totalPage){
                pages.add(page + i);
            }
        }
        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / (size + 1);
        }
        if (page < 1){
            page =1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        this.page = page;

        //是否展示上一页
        showPrevious = page.equals(1)?false:true;
        //是否展示下一页
        showNext = page.equals(totalPage)?false:true;
        //是否展示首页
        showFirstPage = page>4?true:false;
        //是否展示最后一页
        showEndPage = (page.equals(totalPage)||page.equals(totalPage-1))?false:true;
//        if (pages.contains(totalPage)){
//            showEndPage = false;
//        }else{
//            showEndPage = true;
//        }

    }
}
