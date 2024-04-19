import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate';

export default createStore ({
    state: {
        repositories: []
    },
    mutations: {
        addRepos (state, repos) {
            state.repositories.push(repos)
        },
        delRepos (state) {
            state.repositories.pop()
        }
    },
    plugins: [createPersistedState()]
})