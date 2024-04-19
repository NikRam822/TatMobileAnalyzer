import { createStore } from 'vuex'

export default createStore ({
    state: {
        repositories: []
    },
    mutations: {
        addRepos (state, repos) {
            state.repositories.push(repos)
        },
    }
})