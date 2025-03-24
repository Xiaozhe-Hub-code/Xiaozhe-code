import service from "../utils/request";

// export:对外暴露次方此用来调用
export function getQuestionListService(params) {
    return service({
        url: "/question/list",
        method: "get",
        params,
    });
}

export function addQuestionService(params = {}) {
    return service({
        url: "/question/add",
        method: "post",
        data: params,
    });
}

export function getQuestionDetailService(questionId) {
    return service({
        url: "/question/detail",
        method: "get",
        params: { questionId },
    });
}

export function editQuestionService(params = {}) {
    return service({
        url: "/question/edit",
        method: "put",
        data: params,
    });
}

export function deleteQuestionService(questionId) {
    return service({
        url: "/question/delete",
        method: "delete",
        params: { questionId }
    });
}