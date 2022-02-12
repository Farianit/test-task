window.onload = function() {
    let socket = new WebSocket(websocketURL);
    socket.onmessage = function (data) {
        const messageJSON = data.data;
        const message = JSON.parse(messageJSON);
        const authorName = message.author.name;
        const messageText = message.text;
        addMessage(authorName, messageText);
    };
    document.getElementById("message-form").addEventListener('submit', closeSocket);
    document.getElementById("refresh-form").addEventListener('submit', closeSocket);

    function closeSocket() {
        socket.close();
    }
}

function addMessage(author, message) {
    const chatBox = document.getElementById('chat-box');
    chatBox.insertAdjacentHTML('afterbegin',`<p>${author}: ${message}</p>`);
}