window.onload = function() {
    let socket = new WebSocket(websocketURL);
    socket.onopen = function(){
        console.log("Web socket open");
    };
    socket.onmessage = function (data) {
        const messageJSON = data.data;
        const message = JSON.parse(messageJSON);
        const authorName = message.author.name;
        const messageText = message.text;
        addMessage(authorName, messageText);
    };
}

function addMessage(author, message) {
    const chatBox = document.getElementById('chat-box');
    chatBox.insertAdjacentHTML('beforeend',`<p>${author}: ${message}</p>`);
    scrollChatToBottom();
}