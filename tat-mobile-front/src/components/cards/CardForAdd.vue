<template>
  <v-col xs="12" sm="6" md="4" lg="3">
    <v-card
      @click="cardAppend = !cardAppend"
      v-if="cardAppend"
      elevation="4"
      rounded="xl"
      height="165"
      class="d-flex justify-center"
      style="min-width: 200px"
    >
      <v-icon
        icon="mdi-plus-circle-outline"
        size="130"
        color="rgb(92, 99, 106)"
        class="align-self-center"
      ></v-icon>
    </v-card>
    <v-card
      v-else
      rounded="xl"
      height="165"
      border="md"
      style="max-width: 500px; min-width: 200px"
    >
      <v-form @submit.prevent="addCard()" class="d-flex flex-column">
        <v-text-field
          required
          :rules="[re.test(rep) || 'Wrong URL']"
          v-model="rep"
          label="Reposytory URL"
          hint="Enter URL GitHub repository"
          type="url"
        ></v-text-field>
        <div v-if="err" class="error">This repository does not exist</div>
        <v-btn
          elevation="2"
          :disabled="!rep || disabler"
          type="submit"
          icon="mdi-plus-circle-outline"
          class="align-self-center"
        ></v-btn>
      </v-form>
    </v-card>
  </v-col>
</template>
<script>
import axios from "axios";
let server_path = import.meta.env.VITE_BACKEND_URL;
export default {
  data() {
    return {
      disabler: false,
      err: false,
      rep: "",
      cardAppend: true,
      re: new RegExp("^https://(github.com|gitlab([^/]+))/([^/]+)/([^/]+)$"),
    };
  },
  methods: {
    async addCard() {
      this.disabler = true;
      let hostadress = server_path + "/api/project/create";
      try {
        if (!this.re.test(this.rep)) {
          throw new Error("Invalid url: " + this.rep);
        }
        await axios.post(hostadress, {
          projectId: 0,
          projectLink: this.rep,
          projectName: this.rep.slice(this.rep.lastIndexOf("/") + 1),
        });
        this.cardAppend = true;
        this.rep = "";
        this.err = false;
      } catch (error) {
        this.err = true;
        console.error("Error fetching repositories:", error);
      }
      this.$emit("get-repos");
      this.disabler = false;
    },
  },
};
</script>
<style scoped>
.error {
  margin: 0px 10px;
  padding: 0px 8px;
  color: rgb(254, 109, 81);
  background-color: rgba(254, 109, 81, 0.2);
  border: 1px solid rgb(254, 109, 81);
  border-radius: 10px;
}
</style>
