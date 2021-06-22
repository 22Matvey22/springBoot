$(document).ready(function () {

    $('.table .eBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');

        $.get(href, function (user, status) {
            $('.myModal #modal-id').val(user.id);
            $('.myModal #modal-firstName').val(user.firstName);
            $('.myModal #modal-lastName').val(user.lastName);
            $('.myModal #modal-age').val(user.age);
            $('.myModal #modal-email').val(user.email);
            $('.myModal #modal-password').val(user.password);
        });

        $('#editModal').modal();
    });

    $('.table .delBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');

        $.get(href, function (user, status) {
            $('#delete-id').val(user.id);
            $('#delete-firstName').val(user.firstName);
            $('#delete-lastName').val(user.lastName);
            $('#delete-age').val(user.age);
            $('#delete-email').val(user.email);
            $('#delete-password').val(user.password);
        });

        $('#editModal').modal();
    });
});
