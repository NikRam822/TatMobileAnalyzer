<template>
  <v-container>
    <v-row>
      <v-col v-for="item in data">
        <v-card height="60" class="pa-3">
          {{ item[0] }} <br />
          {{ item[1] }}
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  <v-container>
    <v-table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Role</th>
          <th>LOC with churn</th>
          <th>Pay</th>
          <th>Coefficient</th>
          <th>Cost per line of code</th>
          <th>Pay for all time</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Timur</td>
          <th><v-btn>Frontend</v-btn></th>
          <td>325</td>
          <td><v-btn>60000 р.</v-btn></td>
          <td><v-btn>1</v-btn></td>
          <td>134р.</td>
          <td>234054р.</td>
        </tr>
        <tr>
          <td>Sasha</td>
          <th><v-btn>Frontend</v-btn></th>
          <td>321</td>
          <td><v-btn>100000р.</v-btn></td>
          <td><v-btn>1.5</v-btn></td>
          <td>200р.</td>
          <td>354230р.</td>
        </tr>
        <tr>
          <td>Nikita</td>
          <th><v-btn>Backend</v-btn></th>
          <td>298</td>
          <td><v-btn>90000р.</v-btn></td>
          <td><v-btn>1.7</v-btn></td>
          <td>255р.</td>
          <td>500433р.</td>
        </tr>
      </tbody>
    </v-table>
  </v-container>
  <v-container>
    <v-btn style="background-color: rgb(197, 226, 21)">Edit</v-btn>
  </v-container>
</template>
<script>
export default {
  data() {
    return {
      data: [
        ["Developers", "3"],
        ["Time", "2.3 h"],
        ["LOC", "5654"],
        ["Pay for month", "570000"],
        ["Full cost of project", "1331100"],
        ["cost of 1 row", "231,87"],
      ],
    };
  },
};
</script>
