document.addEventListener('DOMContentLoaded', function () {
    var openModalBtn = $('#openModalBtn');
    var modal = $('#myModal');
    var openModalUpdateBtn = $('#openModalUpdateBtn');
    var modalUpdate = $('#modalUpdate');

    openModalBtn.on('click', function () {
        modal.modal('show');
    });

    openModalUpdateBtn.on('click', function () {
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

    function request(method, form) {
        console.log(new URLSearchParams(new FormData(form)).toString())
        fetch('workTime?' + new URLSearchParams(new FormData(form)).toString(), {
            method: method
        })
            .then(response => {
                if (response.ok) {
                    alert("Sucesso!");
                    window.location.href = 'workTime?type=list';
                } else {
                    alert('Ocorreu um erro inesperado');
                    console.error('Erro na requisição. Status:', response.status);
                }
            })
            .catch(error => {
                alert('Ocorreu um erro inesperado: ' + error.message);
                console.error('Erro na requisição:', error);
            });
    }
});


// $(document).ready(function () {
//
//     console.log("passsei")
//     $('#modalUpdate').on('show.bs.modal', function (event) {
//         // Aqui você pode preencher os campos do formulário com os dados do workTimeDTO
//         var workTimeDTO = new Gson().toJson(request.getAttribute("workTimeDTO"));
//
//         $('#updateInput').val(workTimeDTO.input);
//         $('#UpdateOutput').val(workTimeDTO.output);
//     });
// });
