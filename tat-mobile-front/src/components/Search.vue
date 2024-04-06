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
        <v-btn size="x-large" @click="getResult" type="submit"> Search </v-btn>
    </v-form>
</template>
<script>
    export default {
        data: () => ({
        repo: "",
        start: "",
        end: "",
        rules: [
            value => {
                if (value) return true

                return 'You must enter URl'
            }
        ]
    }),
    methods: {
        getResult() {
            this.$emit('get-result', this.repo, this.start, this.end)
        }
    }
    }
</script>