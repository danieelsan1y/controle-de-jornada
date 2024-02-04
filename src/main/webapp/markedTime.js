document.addEventListener('DOMContentLoaded', function () {
    var openModalBtn = $('#openModalBtn');
    var modal = $('#myModal');
    var openModalUpdateBtn = $('.openModalUpdateBtn');
    var modalUpdate = $('#modalUpdate');

    openModalBtn.on('click', function () {
        modal.modal('show');
    });

    openModalUpdateBtn.on('click', function () {
        $('#updateId').val($(this).data('rowid'));
        $('#updateInput').val($(this).data('rowinput'));
        $('#updateOutput').val($(this).data('rowoutput'));
        modalUpdate.modal('show');
    });

    var myForm = document.getElementById('myForm');
    var updateForm = document.getElementById('updateForm');

    updateForm.addEventListener('submit', function (event) {
        event.preventDefault();
        request('PUT', updateForm);
    })

    myForm.addEventListener('submit', function (event) {
        event.preventDefault();
        request('POST', myForm);
    });
});

function request(method, form) {
    fetch('markedTime?' + new URLSearchParams(new FormData(form)).toString(), {
        method: method
    })
        .then(response => {
            if (response.ok) {
                alert("Sucesso!");
                window.location.href = 'markedTime?type=list';
            } else {
                return response.text();
            }
        })
        .then(errorMessage => {
            const match = errorMessage.match(/clientError:(.*?)\./);
            const errorMessageText = match ? match[1].trim() : 'Ocorreu um erro inesperado';

            console.log(errorMessage)
            alert(errorMessageText);
        })
        .catch(error => {
            console.error('Erro na requisição:', error);
        });
}
