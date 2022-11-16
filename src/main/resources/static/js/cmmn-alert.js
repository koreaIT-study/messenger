function cmmnAlert() {
    return {
        getFailed: function () {
            alert('조회를 실패했습니다.');
        },

        postFailed: function () {
            alert('처리를 실패했습니다.');
        },
    }
}

const _cmmnAlert = cmmnAlert();