<%@ page import="com.trackensure.testtask.model.Message" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Log in to chat</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/chat.css" />
</head>
<body>
<div class="container">
    <div class="col-4 offset-4 mt-5">
        <form method="post">
            <input type="hidden" name="name" value="<%=request.getParameter("name")%>">
            <input class="form-control" type="submit" value="Refresh">
        </form>
        <div id="chat-box" class="p-1 mt-1">
            <% for (Message message : (Set<Message>) request.getAttribute("messages")) { %>
            <p><%=message.getAuthor().getName() + ": " + message.getText()%></p>
            <% } %>
        </div>
        <form method="post" class="mt-2">
            <div class="row">
                <div class="col-9">
                    <input type="text" name="text" class="form-control">
                </div>
                <div class="col">
                    <input type="hidden" name="name" value="<%=request.getParameter("name")%>">
                    <input class="form-control" type="submit" value="Send">
                </div>
            </div>
        </form>
    </div>
</div>
<script>
function scrollChatToBottom() {
    let objDiv = document.getElementById("chat-box");
    objDiv.scrollTop = objDiv.scrollHeight;
}
scrollChatToBottom();

let websocketURL = "<%="ws://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/websocket"%>";
</script>
<script src="${pageContext.request.contextPath}/static/js/websocket.js"></script>
</body>
</html>