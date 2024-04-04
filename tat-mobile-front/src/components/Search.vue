<template>
    <v-form @submit.prevent>
        <v-row>
            <v-col>
                <v-container>
                    <v-text-field :rules="rules" v-model="repo" label="Enter repository URL"></v-text-field>
                </v-container>
            </v-col>
            <v-col>
                <v-container>
                    <v-text-field v-model="start" label="Start date"></v-text-field>
                    <v-text-field v-model="end" label="End date"></v-text-field>
                </v-container>
            </v-col>
        </v-row>
        <v-btn size="x-large" @click="getRepos" type="submit"> Search </v-btn>
        <v-progress-circular v-if="switch" indeterminate size="50" width="10"
              color="grey-darken-3"></v-progress-circular>
    </v-form>
</template>
<script>
import axios from 'axios'
export default {
    data: () => ({
        repo: "",
        start: "",
        end: "",
        switch: false,
        rules: [
            value => {
                if (value) return true

                return 'You must enter URl'
            }
        ]
    }),
    methods: {
        async getRepos() {
            this.switch = true
            try {
                const response = await axios.post('http://localhost:8080/patch/statistic', {
                    repositoryUrl: this.repo,
                    since: this.start,
                    until: this.end,
                });
                this.$store.dispatch('updateResult', response.data);
            } catch (error) {
                console.error('Error fetching repositories:', error);
            }
            this.switch = false
        }
    }
}
</script>