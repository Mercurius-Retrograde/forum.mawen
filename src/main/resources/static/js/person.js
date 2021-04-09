/**
 * 提交回复
 * **/
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment-content").val();
    if(!content){
        alert("不能啥也不回复嗷")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success: function (response) {
            if(response.code == 200){
                // $("#comment-section").hide();
                window.location.reload();
            }else{
                if(response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=bccffdfe93d559a50885&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable" , true);
                    }
                }else {
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
    // console.log(questionId);
    // console.log(content);
}
/**
 * 评论列表中二级评论
 * **/
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    //获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if(collapse){
        //折叠二级评论
        comments.remove("in");
    }else{
        //展开评论中评论
        comments.addClass("in");
        //标记二级评论的展开状态
        e.setAttribute("data-collapse","in");
    }
    // console.log(id);
    // $("menu").toggleClass("content-son");
}