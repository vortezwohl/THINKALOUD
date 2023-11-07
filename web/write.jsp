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
        <title>Think Aloud - Write</title>
        <link rel="stylesheet" href="https://template-1253409072.cos.ap-guangzhou.myqcloud.com/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="CSS/index.css"/>
        <script src="JavaScript/JQuery/jquery-3.7.1.min.js"></script>
        <script src="JavaScript/BeijingTimeFormat.js"></script>
        <style>
            select{
                background-color: #ffb5f7;
                color: #7f1072;
                border-width: 0;
                border-radius: 5px;
            }
            .write-space{
                margin-top: 120px;
                position:fixed;
                width: 100vw;
                justify-items: center;
                align-items: center;
                flex-direction: column;
                display: flex;
                padding-top: 50px;
            }
            .input-title{
                text-align: center;
                width: 70rem;
                height: 35px;
                border-radius: 5px;
                border-color: #ff35ec;
                background-color: #f7b0ff57;
                color: #7f1072;
                transition: background-color 0.5s, color 0.5s;
            }
            .input-title:hover{
                background-color: #ffcdfd;
                color: #0f0318
            }
            .input-content{
                margin-top: 15px;
                width: 70rem;
                height: 50vh;
                min-height: 40vh;
                border-radius: 5px;
                border-color: #ff35ec;
                background-color: #f7b0ff57;
                color: #7f1072;
                transition: background-color 0.5s, color 0.5s;
            }
            .input-content:hover{
                background-color: #ffcdfd;
                color: #0f0318
            }
        </style>
    </head>
    <body>
        <div class="container">
            <nav class="navbar navbar-expand-lg">
                <div class="c4944">Think Aloud</div>
                <div class="collapse navbar-collapse">
                    <ul class="navbar-nav justify-content-end ml-auto">
                        <li id="comment_" class="nav-item">
                            <a href="${pageContext.request.contextPath}/" class="btn ml-md-3">Abort | Go back.</a>
                        </li>
                    </ul>
                    <a href="JavaScript:saveDiary()" class="btn ml-md-3">Save | Upload.</a>
                </div>
            </nav>
            <div id="showUser" class="navbar">
                <h3 class="c1445" style="color: rgb(255, 79, 241);">Hey, <%= user.getAccount() %>! What's on your mind?</h3>
            </div>
        </div>
        <!-- Writing Area -->
        <div class="write-space">
            <div class="privacy-selection">
                <label for='privacy' style="color: #6b2864;">Who can see </label>
                <select id='privacy'>
                    <option value=1>Only me</option>
                    <option value=0>Anyone</option>
                </select>
            </div><br/>
            <input id="title" class="input-title" placeholder="Title"/><br/>
            <textarea id="content" class="input-content" placeholder="  Content"></textarea><br/>
        </div>
    <script>
        const saveDiary = () => {
            let datetime = getTimeInBeijingFormat();
            let privacy = document.getElementById("privacy").value;
            let writerId = <%= user.getId() %>;
            let title = document.getElementById("title").value;
            let content = document.getElementById("content").value;
            if(title !== '' && content !== '') {
                $.ajax({
                    url: "save-diary",
                    data: {
                        datetime: datetime,
                        privacy: privacy,
                        writerId: writerId,
                        title: title,
                        content: content
                    },
                    type: 'POST',
                    dataType: "json",
                    success: (result) => {
                        if (!result.result) {
                            alert("Oops! something's not right.");
                            console.log('failed to save diary');
                        }
                    },
                    error: (error) => {
                        console.log(error);
                    },
                });
            }
        }
    </script>
    </body>
</html>