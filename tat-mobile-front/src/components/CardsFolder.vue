<template>
    <v-container fluid>
        <v-row>
            <v-col v-for="val, url in this.$store.state.repositories" cols="auto">
                <v-card @click="navigateToOtherPage(url)" :title="url.slice(url.lastIndexOf('/') + 1)" :subtitle="url"
                    rounded="xl" height="165" width="400" border="md">
                    <template v-slot:append>
                        <v-btn flat @click.stop="this.$store.commit('delRepos', url)" icon="mdi-trash-can-outline">
                        </v-btn>
                    </template>
                    <v-card-text class="d-flex flex-wrap">
                        <v-sheet style="background-color: rgb(92, 99, 106);"
                            v-for="val, name in this.$store.state.repositories[url].churn" class="ma-1 px-1">{{ name
                            }}</v-sheet>
                    </v-card-text>
                </v-card>
            </v-col>
            <v-col cols="auto">

                <v-card @click="cardAppend = !cardAppend" v-if="cardAppend" rounded="xl" height="165" width="400"
                    border="md" class="d-flex justify-center">
                    <v-icon icon="mdi-plus-circle-outline" size="130" color="rgb(92, 99, 106)"
                        class="align-self-center"></v-icon>
                </v-card>

                <v-card v-else rounded="xl" height="165" width="400" border="md">
                    <v-form @submit.prevent="addCard()" class="d-flex flex-column">
                        <v-text-field required :rules="[re.test(rep) || 'Wrong URL']" v-model="rep"
                            label="Enter reposytory URL" type="url"></v-text-field>
                        <v-container v-show="loader" class="mt-2">
                            <v-row justify="center">
                                <v-progress-linear color="rgb(92, 99, 106)" height="6" indeterminate
                                    rounded></v-progress-linear>
                                <p> Analyzing reposytory </p>
                            </v-row>
                        </v-container>
                        <v-btn v-show="!loader" elevation="2" :disabled="!rep" type="submit"
                            icon="mdi-plus-circle-outline" class="align-self-center"></v-btn>
                    </v-form>
                </v-card>

            </v-col>
        </v-row>
    </v-container>
</template>
<script>
import axios from 'axios'
export default {
    data: () => ({
        re: new RegExp("^https://github.com/([^/]+)/([^/]+)$"),
        cardAppend: true,
        rep: '',
        result: [],
        loader: false,
    }),
    methods: {
        async addCard() {
            this.loader = true
            if (this.re.test(this.rep)) {
            } else {
                alert('URl should be: https://github.com/AUTOR/REPO')
            }
            let hostadress = "http://localhost:8080/patch/statistic"
            try {
                const response = await axios.post(hostadress, {
                    repositoryUrl: this.rep,
                });
                this.result = response.data
                this.$store.commit('addRepos', [this.rep, this.result])
            } catch (error) {
                console.error('Error fetching repositories:', error);
            }
            this.rep = ''
            this.cardAppend = true
            this.loader = false
        },
        navigateToOtherPage(repoName) {
            this.$router.push({ path: '/project-review', query: { repoName: repoName } });
        }
    }
}
</script>
