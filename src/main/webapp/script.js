function loadPage(page) {
    $.ajax({
        url: page,
        type: 'GET',
        dataType: 'html',
        success: function (data) {
            $('#dynamicContent').html(data);
        },
        error: function () {
            alert('Erro ao carregar a página.');
        }
    });
}
