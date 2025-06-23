let stompClient = null;

function connect(username) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Đã kết nối: ' + frame);

        stompClient.subscribe('/user/queue/messages', function (message) {
            const msg = JSON.parse(message.body);
            showMessage(msg);
        });
    });
}

function sendMessage(sender, recipient, content) {
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
        sender: sender,
        recipient: recipient,
        content: content,
        timestamp: new Date().toISOString()
    }));
    document.getElementById("messageInput").value = "";
}

function showMessage(msg) {
    const chatBox = document.getElementById("chatBox");
    const p = document.createElement("p");
    p.innerHTML = `<b>${msg.sender}:</b> ${msg.content}`;
    chatBox.appendChild(p);
}
