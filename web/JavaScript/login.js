const httpGet = () => {
    if($("#user").val()!==""&&$("#pass").val()!=="") {
        $.ajax({
            url: "login",
            data: {
                user: $("#user").val(),
                pass: $("#pass").val()
            },
            type: 'GET',
            dataType: "json",
            success: (user) => {
                if (user.isAdmin)
                    window.location.href = "management.jsp";
                else if(user.banned)
                    alert("Sorry, you have been banned :(");
                else
                    window.location.href = "/";
            },
            error: (error) => {
                console.log('Error:', error);
            }
        })
    }
    else
        alert("Please input your information :)");
};