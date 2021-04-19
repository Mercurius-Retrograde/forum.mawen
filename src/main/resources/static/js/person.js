/**
 * 提交问题回复
 * **/
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment-content").val();
    comment2Target(questionId, 1, content);
    // if(!content){
    //     alert("不能啥也不回复嗷")
    //     return;
    // }
    // $.ajax({
    //     type: "POST",
    //     url: "/comment",
    //     contentType:"application/json",
    //     data:JSON.stringify({
    //         "parentId":questionId,
    //         "content":content,
    //         "type":1
    //     }),
    //     success: function (response) {
    //         if(response.code == 200){
    //             // $("#comment-section").hide();
    //             window.location.reload();
    //         }else{
    //             if(response.code == 2003){
    //                 var isAccepted = confirm(response.message);
    //                 if(isAccepted){
    //                     window.open("https://github.com/login/oauth/authorize?client_id=bccffdfe93d559a50885&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
    //                     window.localStorage.setItem("closable" , true);
    //                 }
    //             }else {
    //                 alert(response.message);
    //             }
    //             // if (response.code == 2008){
    //             //     alert(response.message)
    //             // }
    //         }
    //         console.log(response);
    //     },
    //     dataType: "json"
    // });
    // console.log(questionId);
    // console.log(content);
}

/**
 * 提交二级评论
 * **/
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $('#input-' + commentId).val();
    comment2Target(commentId, 2, content);
}

/**
 * 封装提交评论方法
 **/
function comment2Target(targetId, type, content) {
    if (!content) {
        alert("不能啥也不回复嗷")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                // $("#comment-section").hide();
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=bccffdfe93d559a50885&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
                // if (response.code == 2008){
                //     alert(response.message)
                // }
            }
            console.log(response);
        },
        dataType: "json"
    });
}

/**
 * 评论列表中二级评论
 * **/
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

// $(comments).toggleClass("in");
// $("").toggleClass("active");
// var collapse = e.getAttribute("data-collapse");
// console.log(comment);
//获取二级评论的展开状态
// if(collapse){
//     //折叠二级评论
//     comments.remove("in");
//     e.removeAttribute("data-collapse");
// }else{
//     //展开评论中评论
//     comments.addClass("in");
//     //标记二级评论的展开状态
//     e.setAttribute("data-collapse","in");
// }

function showSelectTag() {
    $("#select-tag").show();
}
function selectTag(value) {
    var previous = $("#tag").val();
    if (previous.indexOf(value) != -1) {
    } else {
        if (previous) {
            $("#tag").val(previous + '，' + value);
        } else
            $("#tag").val(value);
    }
}