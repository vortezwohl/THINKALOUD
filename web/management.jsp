<%@ page import="com.wohl.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! private User user = new User(); %>
<%
    if(session.getAttribute("user") == null)
        response.sendRedirect("/login.jsp");
    else {
        user = (User) session.getAttribute("user");
        if(user.getIsAdmin() == 0)
            response.sendRedirect("/login.jsp");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>System - Admin</title>
        <script src="JavaScript/JQuery/jquery-3.7.1.min.js"></script>
        <script src="JavaScript/BeijingTimeFormat.js"></script>
        <link rel="stylesheet" href="CSS/management.css"/>
    </head>
    <body>
        <div id="console">
            <div id="output"></div>
        </div>
        <input type="text" id="input" placeholder="Command"/>
        <script>
            let outputBlock = $("#output");
            outputBlock.html("<div class='systemSpec'>THINK ALOUD management system started ;)</div>");
            document.getElementById("input").addEventListener('keydown',async (event) => {
                let outputBlock = document.getElementById("output");
                if(event.key === 'Enter'){
                    let cmd = document.getElementById("input").value;
                    outputBlock.innerHTML += '<div class="systemIn">${sessionScope.user.getAccount()}>>'+cmd+'</div>';
                    //TODO: system output
                    let output = await processCmd(cmd);
                    if(output) {
                        if(output !== 'nothing'){
                            outputBlock.innerHTML += '<div class="systemOut">' + output + '</div>';
                            if(output === "Good bye ${sessionScope.user.getAccount()}! ;)"){
                                setTimeout(()=>{
                                    window.location.href = "login.jsp";
                                },1000);
                            }
                            else if(output === "Emptied"){
                                outputBlock.innerHTML = "<div class='systemSpec'>THINK ALOUD management system Emptied ;)</div>";
                            }
                        }
                    }
                    else
                        outputBlock.innerHTML += '<div class="systemErr">Invalid command "'+cmd+'".</div>';
                    document.getElementById("input").value = '';
                    let consoleView = document.getElementById('console');
                    consoleView.scrollTop = consoleView.scrollHeight;
                }
                else if(event.key === 'ArrowUp'){
                    let parentElement = document.getElementById('output');
                    let lastCmd = parentElement.children[parentElement.children.length - 2].innerHTML.split("&gt;&gt;")[1];
                    document.getElementById("input").value = lastCmd?lastCmd:'';
                }
            });
            const processCmd = async (cmd) => {
                let cmdStmt = cmd.split(' ');
                switch (cmdStmt[0]) {
                    default:
                        return null;
                    case 'help':
                    case 'manual':
                    case 'guide':
                        return '<div class="systemOut">' +
                            '<div class="systemSpec">Quick Guide To Think Aloud Management System</div>' +
                            '-----------------------------------------------------------------------------------------------------------------------------------------------</br>' +
                            'help/manual/guide: show instruction guide.</br>' +
                            '//[space][something]/--[space][something]: leave annotations.</br>' +
                            'echo[Input]: print your input to screen.</br>' +
                            'exit/bye/quit: log out from system.</br>' +
                            'clear/refresh/cls: clear screen.</br>' +
                            'freeze/frz[-u/-user|-d/-diary][Id]: freeze existed user by UserId.</br>' +
                            'unfreeze/defrost/dfr[-u/-user|-d/-diary][Id]: unfreeze existed user by UserId.</br>' +
                            'read/type/cat[Id]: load a diary to screen by DiaryId.</br>' +
                            'show[scope][-filter/-f][filter]: list users or diaries under conditions or list them all without precondition(scope could be users or diaries).</br>' +
                            '-----------------------------------------------------------------------------------------------------------------------------------------------' +
                            '</div>';
                    case '':
                    case '//':
                    case '--':
                        return 'nothing';
                    case 'echo':
                        return cmdStmt[1];
                    case 'exit':
                    case 'bye':
                    case 'quit':
                        return "Good bye ${sessionScope.user.getAccount()}! ;)";
                    case 'cls':
                    case 'clear':
                    case 'refresh':
                        return "Emptied";

                    case 'freeze':
                    case 'frz':
                        if(Number.isInteger(parseInt(cmdStmt[2]))&&(cmdStmt[1] === '-u' || cmdStmt[1] === '-user'))
                            return banUser(cmdStmt[2]);
                        else if(Number.isInteger(parseInt(cmdStmt[2]))&&(cmdStmt[1] === '-d' || cmdStmt[1] === '-diary'))
                            return lockDiary(cmdStmt[2]);
                        else
                            return '<div class="systemErr">Invalid parameter for command "freeze / frz"</br>Command Guide: freeze / frz [-u / -user | -d / -diary] [Id].</div>';
                    case 'unfreeze':
                    case 'defrost':
                    case 'dfr':
                        if(Number.isInteger(parseInt(cmdStmt[2]))&&(cmdStmt[1] === '-u' || cmdStmt[1] === '-user'))
                            return unbanUser(cmdStmt[2]);
                        else if(Number.isInteger(parseInt(cmdStmt[2]))&&(cmdStmt[1] === '-d' || cmdStmt[1] === '-diary'))
                            return unlockDiary(cmdStmt[2]);
                        else
                            return '<div class="systemErr">Invalid parameters for command "unfreeze / defrost / dfr"</br>Command Guide: unfreeze / defrost / dfr [-u / -user | -d / -diary] [Id].</div>';

                    case 'read':
                    case 'type':
                    case 'cat':
                        if(Number.isInteger(parseInt(cmdStmt[1]))){
                            let diary = await readDiary(cmdStmt[1]);//TODO finish
                            if(diary === "<div class='systemErr'>Diary#"+cmdStmt[1]+" doesn't exist</div>")
                                return diary;
                            else {
                                let title = diary.title;
                                let content = diary.content;
                                let accountOfWriter = diary.accountOfWriter;
                                let datetime = diary.datetime;
                                return '<div class="systemOut">Title: '+title+'</br>Author: '+accountOfWriter+'</br>Datetime: '+datetime+'</br>Content: '+content+'</div>';
                            }
                        }
                        else
                            return '<div class="systemErr">Invalid parameters for command "read / type / cat"</br>Command Guide: read / type / cat [Id].</div>';
                    case 'show':
                        if(cmdStmt[1] === 'users'){
                            if(cmdStmt[2] === '-filter' || cmdStmt[2] === '-f'){
                                if(cmdStmt[3] === 'frozen'){
                                    let userList = await showUsers();
                                    let result = '';
                                    for (let index = 0; index < userList.length; index++) {
                                        let id = userList[index].id;
                                        let banned = userList[index].banned ? 'Frozen' : 'Unfrozen';
                                        let isAdmin = userList[index].isAdmin ? "Admin" : "Client";
                                        let account = userList[index].account;
                                        if (userList[index].banned)
                                            result = result.concat("<div class='entityErr'>UserId: ", id, ", Account: ", account, ", Authority: ", isAdmin, ", Status: ", banned, '</div>');
                                    }
                                    if(result !== '')
                                        return result;
                                    else
                                        return "None";
                                }
                                else
                                    return '<div class="systemErr">Invalid parameters for command "show"</br>Command Guide: show [scope] [-filter / -f] [filter].</div>';
                            }
                            else if(cmdStmt[2] == null){
                                let userList = await showUsers();
                                let result = '';
                                for (let index = 0; index < userList.length; index++) {
                                    let id = userList[index].id;
                                    let banned = userList[index].banned ? 'Frozen' : 'Unfrozen';
                                    let isAdmin = userList[index].isAdmin ? "Admin" : "Client";
                                    let account = userList[index].account;
                                    if (userList[index].banned)
                                        result = result.concat("<div class='entityErr'>UserId: ", id, ", Account: ", account, ", Authority: ", isAdmin, ", Status: ", banned, '</div>');
                                    else
                                        result = result.concat("UserId: ", id, ", Account: ", account, ", Authority: ", isAdmin, ", Permission: ", banned, '</br>');
                                }
                                if(result !== '')
                                    return result;
                                else
                                    return "None";
                            }
                            else
                                return '<div class="systemErr">Invalid parameters for command "show"</br>Command Guide: show [scope] [-filter / -f] [filter].</div>';
                        }
                        else if(cmdStmt[1] === 'diaries'){
                            if(cmdStmt[2] === '-filter' || cmdStmt[2] === '-f'){
                                if(cmdStmt[3] === 'frozen'){
                                    let diaryList = await showDiaries();
                                    let result = '';
                                    for (let index = 0; index < diaryList.length; index++) {
                                        let id = diaryList[index].id;
                                        let writerId = diaryList[index].idOfWriter;
                                        let writer = diaryList[index].accountOfWriter;
                                        let title = diaryList[index].title;
                                        let datetime = diaryList[index].datetime;
                                        let Visibility = diaryList[index]._private ? 'Private' : 'Published';
                                        let locked = diaryList[index].locked ? 'Frozen' : 'Unfrozen';
                                        let reported = diaryList[index].reported ? 'Reported' : 'Not Reported';
                                        let reportReason = diaryList[index].reportReason;
                                        if (diaryList[index].locked) {
                                            result = result.concat("<div class='entityErr'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                        }
                                    }
                                    if(result !== '')
                                        return result;
                                    else
                                        return "None";
                                }
                                else if(cmdStmt[3] === 'reported'){
                                    let diaryList = await showDiaries();
                                    let result = '';
                                    for (let index = 0; index < diaryList.length; index++) {
                                        let id = diaryList[index].id;
                                        let writerId = diaryList[index].idOfWriter;
                                        let writer = diaryList[index].accountOfWriter;
                                        let title = diaryList[index].title;
                                        let datetime = diaryList[index].datetime;
                                        let Visibility = diaryList[index]._private ? 'Private' : 'Published';
                                        let locked = diaryList[index].locked ? 'Frozen' : 'Unfrozen';
                                        let reported = diaryList[index].reported ? 'Reported' : 'Not Reported';
                                        let reportReason = diaryList[index].reportReason;
                                        if (diaryList[index].reported) {
                                            result = result.concat("<div class='entityRpt'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                        }
                                    }
                                    if(result !== '')
                                        return result;
                                    else
                                        return "None";
                                }
                                else if(cmdStmt[3] === 'in-day'){
                                    let diaryList = await showDiaries();
                                    let result = '';
                                    let day = '';
                                    let today = '';
                                    let month = '';
                                    let thisMonth = '';
                                    let year = '';
                                    let thisYear = '';
                                    for (let index = 0; index < diaryList.length; index++) {
                                        let id = diaryList[index].id;
                                        let writerId = diaryList[index].idOfWriter;
                                        let writer = diaryList[index].accountOfWriter;
                                        let title = diaryList[index].title;
                                        let datetime = diaryList[index].datetime;
                                        let Visibility = diaryList[index]._private ? 'Private' : 'Published';
                                        let locked = diaryList[index].locked ? 'Frozen' : 'Unfrozen';
                                        let reported = diaryList[index].reported ? 'Reported' : 'Not Reported';
                                        let reportReason = diaryList[index].reportReason;
                                        if (diaryList[index].locked) {
                                            result = result.concat("<div class='entityErr'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                        } else if (diaryList[index].reported) {
                                            result = result.concat("<div class='entityRpt'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                        } else {
                                            result = result.concat("DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</br>");
                                        }
                                        day = datetime.split(' ')[0].split('-')[2];
                                        today = getTimeInBeijingFormat().split(' ')[0].split('-')[2];
                                        month = datetime.split(' ')[0].split('-')[1];
                                        thisMonth = getTimeInBeijingFormat().split(' ')[0].split('-')[1];
                                        year = datetime.split(' ')[0].split('-')[0];
                                        thisYear = getTimeInBeijingFormat().split(' ')[0].split('-')[0];
                                    }
                                    if(result !== '' && day === today && month === thisMonth && year === thisYear)
                                        return result;
                                    else
                                        return "None";
                                }
                                else if(cmdStmt[3] === 'in-month'){
                                    let diaryList = await showDiaries();
                                    let result = '';
                                    let month = '';
                                    let thisMonth = '';
                                    let year = '';
                                    let thisYear = '';
                                    for (let index = 0; index < diaryList.length; index++) {
                                        let id = diaryList[index].id;
                                        let writerId = diaryList[index].idOfWriter;
                                        let writer = diaryList[index].accountOfWriter;
                                        let title = diaryList[index].title;
                                        let datetime = diaryList[index].datetime;
                                        let Visibility = diaryList[index]._private ? 'Private' : 'Published';
                                        let locked = diaryList[index].locked ? 'Frozen' : 'Unfrozen';
                                        let reported = diaryList[index].reported ? 'Reported' : 'Not Reported';
                                        let reportReason = diaryList[index].reportReason;
                                        if (diaryList[index].locked) {
                                            result = result.concat("<div class='entityErr'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                        } else if (diaryList[index].reported) {
                                            result = result.concat("<div class='entityRpt'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                        } else {
                                            result = result.concat("DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</br>");
                                        }
                                        month = datetime.split(' ')[0].split('-')[1];
                                        thisMonth = getTimeInBeijingFormat().split(' ')[0].split('-')[1];
                                        year = datetime.split(' ')[0].split('-')[0];
                                        thisYear = getTimeInBeijingFormat().split(' ')[0].split('-')[0];
                                    }
                                    if(result !== '' && month === thisMonth && year === thisYear)
                                        return result;
                                    else
                                        return "None";
                                }
                                else if(cmdStmt[3] === 'in-year') {
                                    let diaryList = await showDiaries();
                                    let result = '';
                                    let year = '';
                                    let thisYear = '';
                                    for (let index = 0; index < diaryList.length; index++) {
                                        let id = diaryList[index].id;
                                        let writerId = diaryList[index].idOfWriter;
                                        let writer = diaryList[index].accountOfWriter;
                                        let title = diaryList[index].title;
                                        let datetime = diaryList[index].datetime;
                                        let Visibility = diaryList[index]._private ? 'Private' : 'Published';
                                        let locked = diaryList[index].locked ? 'Frozen' : 'Unfrozen';
                                        let reported = diaryList[index].reported ? 'Reported' : 'Not Reported';
                                        let reportReason = diaryList[index].reportReason;
                                        if (diaryList[index].locked) {
                                            result = result.concat("<div class='entityErr'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                        } else if (diaryList[index].reported) {
                                            result = result.concat("<div class='entityRpt'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                        } else {
                                            result = result.concat("DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                            result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</br>");
                                        }
                                        year = datetime.split(' ')[0].split('-')[0];
                                        thisYear = getTimeInBeijingFormat().split(' ')[0].split('-')[0];
                                    }
                                    if(result !== '' && year === thisYear)
                                        return result;
                                    else
                                        return "None";
                                }
                                else
                                    return '<div class="systemErr">Invalid parameters for command "show"</br>Command Guide: show [scope] [-filter / -f] [filter].</div>';
                            }
                            else if(cmdStmt[2] == null){
                                let diaryList = await showDiaries();
                                let result = '';
                                for (let index = 0; index < diaryList.length; index++) {
                                    let id = diaryList[index].id;
                                    let writerId = diaryList[index].idOfWriter;
                                    let writer = diaryList[index].accountOfWriter;
                                    let title = diaryList[index].title;
                                    let datetime = diaryList[index].datetime;
                                    let Visibility = diaryList[index]._private ? 'Private' : 'Published';
                                    let locked = diaryList[index].locked ? 'Frozen' : 'Unfrozen';
                                    let reported = diaryList[index].reported ? 'Reported' : 'Not Reported';
                                    let reportReason = diaryList[index].reportReason;
                                    if (diaryList[index].locked) {
                                        result = result.concat("<div class='entityErr'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                        result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                    } else if (diaryList[index].reported) {
                                        result = result.concat("<div class='entityRpt'>DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                        result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</div>");
                                    } else {
                                        result = result.concat("DiaryId: ", id, ", WriterId(UserId): ", writerId, ", Writer: ", writer, ", Title: ", title);
                                        result = result.concat(", Datetime: ", datetime, ", Visibility: ", Visibility, ", Status: ", locked, ", Report: ", reported, ", Report&FeedBack: ", reportReason, "</br>");
                                    }
                                }
                                if(result !== '')
                                    return result;
                                else
                                    return "None";
                            }
                            else
                                return '<div class="systemErr">Invalid parameters for command "show"</br>Command Guide: show [scope] [-filter / -f] [filter].</div>';
                        }
                        else
                            return '<div class="systemErr">Invalid parameters for command "show"</br>Command Guide: show [scope] [-filter / -f] [filter].</div>';
                }
            };
            const showUsers = async () => {
                let userList = null;
                await $.ajax({
                    url: 'list-all-users',
                    type: 'GET',
                    dataType: 'json',
                    success: (users) => {
                        userList = users;
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });
                return userList;
            };
            const showDiaries = async () => {
                let diaryList = null;
                await $.ajax({
                    url: 'list-all-diaries',
                    type: 'GET',
                    dataType: 'json',
                    success: (diaries) => {
                        diaryList = diaries;
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });
                return diaryList;
            };
            const banUser = async (id) => {
                let report = '';
                await $.ajax({
                    url: 'ban-user-by-id',
                    type: 'GET',
                    data: {
                        userId: id
                    },
                    dataType: 'json',
                    success: (result) => {
                        if(result.result)
                            report = 'User#'+id+' frozen';
                        else
                            report = "<div class='systemErr'>User#"+id+" doesn't exist</div>";
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });
                return report;
            };
            const unbanUser = async (id) => {
                let report = '';
                await $.ajax({
                    url: 'unban-user-by-id',
                    type: 'GET',
                    data: {
                        userId: id
                    },
                    dataType: 'json',
                    success: (result) => {
                        if(result.result)
                            report = 'User#'+id+' unfrozen';
                        else
                            report = "<div class='systemErr'>User#"+id+" doesn't exist</div>";
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });
                return report;
            };
            const lockDiary = async (id) => {
                let report = '';
                await $.ajax({
                    url: 'lock-diary-by-id',
                    type: 'GET',
                    data: {
                        diaryId: id
                    },
                    dataType: 'json',
                    success: (result) => {
                        if(result.result)
                            report = 'Diary#'+id+' frozen';
                        else
                            report = "<div class='systemErr'>Diary#"+id+" doesn't exist</div>";
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });
                return report;
            };
            const unlockDiary = async (id) => {
                let report = '';
                await $.ajax({
                    url: 'unlock-diary-by-id',
                    type: 'GET',
                    data: {
                        diaryId: id
                    },
                    dataType: 'json',
                    success: (result) => {
                        if(result.result)
                            report = 'Diary#'+id+' unfrozen';
                        else
                            report = "<div class='systemErr'>Diary#"+id+" doesn't exist</div>";
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });
                return report;
            };
            const readDiary = async (id) => {
                let thatDiary = null;
                await $.ajax({
                    url: 'fetch-diary-by-id',
                    type: 'GET',
                    data: {
                        diaryId: id
                    },
                    dataType: 'json',
                    success: (result) => {
                        if(result.result)
                            thatDiary = "<div class='systemErr'>Diary#"+id+" doesn't exist</div>";
                        else
                            thatDiary = result;
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });
                return thatDiary;
            };
        </script>
    </body>
</html>