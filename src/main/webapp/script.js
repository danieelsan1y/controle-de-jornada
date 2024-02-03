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

document.addEventListener("DOMContentLoaded", function () {
    var navbarContainer = document.getElementById("navbar-container");

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
