function loadPage(page) {
    $.ajax({
        url: page,
        type: 'GET',
        dataType: 'html',
        error: function () {
            alert('Erro ao carregar a página.');
        }
    });
}

function closeModal(page) {
    window.location.href = page;
}

document.addEventListener("DOMContentLoaded", function () {
    const navbarContainer = document.getElementById("navbar-container");

    if (navbarContainer) {
        fetch("nav.html")
            .then(response => response.text())
            .then(data => {
                navbarContainer.innerHTML = data;
            })
            .catch(error => console.error("Erro ao carregar a navegação:", error));
    } else {
        console.error("Elemento com ID 'navbar-container' não encontrado.");
    }
});

function updateClock() {
    const currentDate = new Date();
    let hours = currentDate.getHours();
    let minutes = currentDate.getMinutes();
    let seconds = currentDate.getSeconds();

    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    seconds = seconds < 10 ? '0' + seconds : seconds;

    document.getElementById('clock').innerText = hours + ':' + minutes + ':' + seconds;

    setTimeout(updateClock, 1000);
}

document.addEventListener('DOMContentLoaded', function () {
    updateClock();
});
