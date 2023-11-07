<%@ page import="com.wohl.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! private User user = new User(); %>
<%
    if(session.getAttribute("user") == null)
        response.sendRedirect("/login.jsp");
    else
        user = (User)session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>Think Aloud - <%= user.getAccount() %>'s Space</title>
        <link rel="stylesheet" href="https://template-1253409072.cos.ap-guangzhou.myqcloud.com/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="CSS/index.css"/>
        <script src="JavaScript/JQuery/jquery-3.7.1.min.js"></script>
        <script src="JavaScript/BeijingTimeFormat.js"></script>
    </head>
    <body>
        <header data-block-type="headers" data-id="1"></header>
        <div class="container">
            <nav class="navbar navbar-expand-lg">
                <div class="c4944">Think Aloud</div>
                <div class="collapse navbar-collapse" style="z-index:1;">
                    <ul class="navbar-nav justify-content-end ml-auto">
                        <li id="edit_" class="nav-item">
                            <!-- was class="nav-link" -->
                            <a href="JavaScript:editDiary()" class="btn ml-md-3">Edit.</a>
                        </li>
                        <li id="remove_"  class="nav-item">
                            <a href="JavaScript:removeDiary()" class="btn ml-md-3">Remove.</a>
                        </li>
                        <li id="report_" class="nav-item">
                            <a href="JavaScript:reportDiary()" class="btn ml-md-3">Report.</a>
                        </li>
                    </ul>
                    <a href="write.jsp" class="btn ml-md-3">Write.</a>
                    <a href="JavaScript:logout()" class="btn ml-md-3">Log Out.</a>
                </div>
            </nav>
            <div id="showUser" class="navbar">
                <h3 class="c1445" style="color: rgb(255, 79, 241);">Welcome, <%= user.getAccount() %>! How was your day?</h3>
            </div>

            <div class="htmlpage-row" style="left: 250px;">
                <div class="htmlpage-cell" id="diary">
                    <!-- diary -->
                    <textarea id="showDiary" class="showDiary" placeholder="Select a diary to read" readonly></textarea>
                    <!-- TODO: 完成评论操作 -->
                    <input id="comment_" class="input-comment" placeholder="  Make a friendly comment"/>
                    <a href="JavaScript:CommentUnderDiary()" class="input-comment-submit">Comment</a>
                    <!--comments-->
                    <div id="showComments" class="showComments">

                    </div>
                </div>
            </div>
        </div>
        <div id="mySidebar" class="sidebar">
            <a href="javascript:openCloseNav()" class="closebtn c1685">Fold / Refresh</a>
            <!-- 这里都是日记标题 -->
        </div>

        <!-- TODO: js logic -->
        <script>
            document.getElementById("edit_").style.display = "none";
            document.getElementById("remove_").style.display = "none";
            const fetchDiaryForClient = () => {
                document.getElementById('mySidebar').innerHTML = '<a href="javascript:openCloseNav()" class="closebtn c1685">Fold / Refresh</a>';
                $.ajax({
                    url: "fetch-diary-client",
                    type: 'GET',
                    data: {
                        userId: <%= user.getId() %>
                    },
                    dataType: "json",
                    success: (diaries) => {
                        for(let index=0; index<diaries.length; index++) {
                            document.getElementById('mySidebar').innerHTML += '<a id=mySlide'+index+' href="javascript:show('+diaries[index].id+')">'+diaries[index].title+' - '+diaries[index].accountOfWriter+'#'+diaries[index].idOfWriter+'</a>';
                        }
                    },
                    error: (error) => {
                        console.log(error);
                    },
                })
            };
            fetchDiaryForClient();
            let count = 1;
            const openCloseNav = () => {
                if(count++%2) {
                    document.getElementById("mySidebar").style.width = "250px";
                    setTimeout(fetchDiaryForClient, 150);
                }
                else {
                    document.getElementById('mySidebar').innerHTML = '<a href="javascript:openCloseNav()" class="closebtn c1685">Fold / Refresh</a>';
                    document.getElementById("mySidebar").style.width = "22px";
                }
            };
            const show = (id) => {
                let userId = <%= user.getId() %>;
                let diaryBlock = document.getElementById("showDiary");
                $.ajax({
                    url: "fetch-diary-by-id",
                    type: 'GET',
                    data: {
                        diaryId: id
                    },
                    dataType: "json",
                    success: (diary) => {
                        diaryBlock.innerHTML = '《'+diary.title+'》\n  Date: '+diary.datetime+'\n  Author: '+diary.accountOfWriter+'\n  Content:\n\t' + diary.content;
                        diaryBlock.diaryId = diary.id;
                        diaryBlock.idOfWriter = diary.idOfWriter;
                        diaryBlock.exists = true;
                        diaryBlock.title = diary.title;
                        diaryBlock.content = diary.content;
                        showComments(diary.id);
                        if(diary.idOfWriter === userId){
                            if(!diary.locked) {
                                document.getElementById("edit_").style.display = "";
                                document.getElementById("remove_").style.display = "";
                            }
                            else{
                                document.getElementById("edit_").style.display = "none";
                                document.getElementById("remove_").style.display = "";
                            }
                        }
                        else{
                            document.getElementById("edit_").style.display = "none";
                            document.getElementById("remove_").style.display = "none";
                        }
                    },
                    error: (error) => {
                        console.log(error);
                    },
                })
            };
            const showComments = (diaryId) => {
                let commentBlock = document.getElementById("showComments");
                $.ajax({
                    url: 'comment-under-diary',
                    data: {
                        idOfDiary: diaryId
                    },
                    type: 'GET',
                    dataType: 'json',
                    success: (comments) => {
                        commentBlock.innerHTML='';
                        for(let index=0; index<comments.length; index++) {
                            commentBlock.innerHTML += '<div id="comment#'+comments[index].id+'" class="commentBlock"><h5>'+comments[index].accountOfWriter+'</h5>'+comments[index].datetime+'<br/><h6>'+comments[index].content+'</h6></div>';
                        }
                    },
                    error: (error) => {
                        console.log(error);
                    },
                });
            };

            //TODO: refresh on comment block
            let interval = 3000;
            setInterval(()=>{
                let diaryBlock = document.getElementById("showDiary");
                showComments(diaryBlock.diaryId);
            },interval);

            const CommentUnderDiary = () => {
                let diaryBlock = document.getElementById("showDiary");
                let idOfDiary = diaryBlock.diaryId;
                let content = document.getElementById("comment_").value;
                let datetime = getTimeInBeijingFormat();
                let idOfWriter = <%= user.getId() %>;
                if(idOfDiary != null && diaryBlock.exists) {
                    if(content !== '') {
                        $.ajax({
                            url: 'comment-under-diary',
                            data: {
                                idOfDiary: idOfDiary,
                                content: content,
                                datetime: datetime,
                                idOfWriter: idOfWriter
                            },
                            type: 'POST',
                            dataType: 'json',
                            success: (result) => {
                                if (!result.result) {
                                    alert("Oops! something went wrong.");
                                    console.log('failed to make comment');
                                }
                                else
                                    showComments(idOfDiary);
                            },
                            error: (error) => {
                                console.log(error);
                            },
                            complete: () => {
                                document.getElementById("comment_").value = '';
                            }
                        });
                    }
                    else
                        alert("Make comment before send comment");
                }
                else
                    alert("Select a diary first");
            };

            const reportDiary = () => {
                let diaryBlock = document.getElementById("showDiary");
                let idOfDiary = diaryBlock.diaryId;
                if(diaryBlock.innerHTML !== '') {
                    let reason = prompt("Why do you report this diary?",'');
                    $.ajax({
                        url: 'report-diary-by-id',
                        data: {
                            idOfDiary: idOfDiary,
                            reason: reason
                        },
                        type: 'GET',
                        dataType: 'json',
                        success: (result) => {
                            if (!result.result) {
                                alert("Oops! something went wrong.");
                                console.log('failed to report');
                            }
                        },
                        error: (error) => {
                            console.log(error);
                        },
                    });
                }
                else
                    alert("Select a diary first");
            };

            const removeDiary = () => {
                let diaryBlock = document.getElementById("showDiary");
                let idOfDiary = diaryBlock.diaryId;
                if(diaryBlock.innerHTML !== '') {
                    $.ajax({
                        url: 'remove-diary',
                        data: {
                            idOfDiary: idOfDiary
                        },
                        type: 'GET',
                        dataType: 'json',
                        success: (result) => {
                            if (!result.result) {
                                alert("Oops! something went wrong.");
                                console.log('failed to remove');
                            } else {
                                diaryBlock.innerHTML = '';
                                document.getElementById("edit_").style.display = "none";
                                document.getElementById("remove_").style.display = "none";
                                diaryBlock.exists = false;
                                fetchDiaryForClient();
                                showComments(diaryBlock.diaryId);
                            }
                        },
                        error: (error) => {
                            console.log(error);
                        },
                    });
                }
            };

            const editDiary = () => {
                let diaryBlock = document.getElementById("showDiary");
                let idOfDiary = diaryBlock.diaryId;
                $.ajax({
                    url: 'edit-diary-router',
                    data: {
                        id: idOfDiary,
                        title: diaryBlock.title,
                        content: diaryBlock.content
                    },
                    type: 'POST',
                    dataType: 'json',
                    success: (result) => {
                        if (result.result)
                            window.location.href = 'edit.jsp';
                        else{
                            alert("Oops! something went wrong.");
                            console.log('failed to remove');
                        }
                    },
                    error: (error) => {
                        console.log(error);
                    },
                })
            };

            const logout = () => {
                $.ajax({
                    url: 'logout',
                    data: {
                        uid: <%= user.getId() %>
                    },
                    type: 'GET',
                    dataType: 'json',
                    success: (result) => {
                        if (result.result)
                            window.location.href = 'login.jsp';
                        else{
                            alert("Oops! something went wrong.");
                            console.log('failed to remove');
                        }
                    },
                    error: (error) => {
                        console.log(error);
                    },
                })
            };
        </script>
    </body>
</html>