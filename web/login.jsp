<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Think Aloud - Login</title>
        <style>
            body{
                display: flex;
                height: 100vh;
                margin: 0;
                background-color: #ffcbf2;
                color: #bdafbb;
                flex-direction: column;
                align-items: center;
            }
            .navbar {
                display: flex;
                height: 60px;
                width: 100vw;
                background-color: rgb(255, 169, 237);
                color: rgb(255, 79, 241);
                overflow-x: hidden;
                overflow-y: hidden;
                justify-content: center;
                align-items: center;
                text-shadow: rgb(23, 5, 23) 3px 2px 4px;
                font-family:Arial, Helvetica, sans-serif;
            }
            .login{
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                width: 100rem;
                height: 40rem;
            }
            .input1{
                margin: 5px 5px;
                text-align: start;
                width: 40rem;
                height: 35px;
                border-radius: 5px;
                border-color: #ff35ec;
                background-color: #ffb0f1;
                color: #7f1072;
                transition: background-color 0.5s, color 0.5s;
            }
            .input1:hover{
                background-color: #ffcdfd;
                color: #c412ff
            }
            .button1{
                margin-top: 20px;
                width: 40rem;
                height: 50px;
                border-color: #ff35ec;
                background-color: #ffb0f1;
                color: #7f1072;
                border-radius: 10px;
                transition: background-color 0.5s,color 0.5s;
            }
            .button1:hover{
                background-color: #ff95ff;
                color: #be00ff
            }
        </style>
    </head>
    <body>
        <div class="navbar">
            <h2>Think Aloud</h2>
        </div>
        <div class="login">
            <input id="user" name="user" type="text" placeholder="  Account" class="input1"/><br/>
            <input id="pass" name="pass" type="password" placeholder="  Password" class="input1"/><br/>
            <button id="submit" name="submit" class="button1" onclick="httpGet()">Log in | Sign in</button><br/>
        </div>
        <script src = "JavaScript/JQuery/jquery-3.7.1.min.js"></script>
        <script src="JavaScript/login.js"></script>
    </body>
</html>
