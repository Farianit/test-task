window.onload = function() {
    axios.get('messages', {})
        .then(function (response){
            response.data.forEach(message => addMessage(message.author.name, message.text, 'afterbegin'));
            scrollChatToBottom();
        });
    let socket = new WebSocket(websocketURL);
    socket.onmessage = function (data) {
        const message = JSON.parse(data.data);
        const authorName = message.author.name;
        const messageText = message.text;
        addMessage(authorName, messageText, 'beforeend');
        scrollChatToBottom();
    };
    document.getElementById("refresh-form").addEventListener('submit', function() {
        socket.close();
    });
    document.getElementById("message-form").addEventListener('submit', function(event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        axios.post(`messages?name=${formData.get('name')}&text=${formData.get('text')}`, {}).catch(function (error) {
            console.log(error);
        });
        document.getElementById('message-input').value = '';
    });
}

function addMessage(author, message, position) {
    const chatBox = document.getElementById('chat-box');
    chatBox.insertAdjacentHTML(position,`<p>${author}: ${message}</p>`);
}

function scrollChatToBottom() {
    let objDiv = document.getElementById("chat-box");
    objDiv.scrollTop = objDiv.scrollHeight;
}