<template>
  <v-row>
    <v-col v-for="item in data[this.switch]">
      <v-card height="60" class="pa-3">
        {{ item[0] }} <br />
        <div>{{ item[1] }}</div>
      </v-card>
    </v-col>
  </v-row>
  <v-table>
    <thead>
      <tr>
        <th>Name</th>
        <th>Pay</th>
        <th>Real pay</th>
        <th>LOC</th>
        <th>Type of work</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in table[this.switch]">
        <td>
          {{ item[0] }}
        </td>
        <td>
          <v-btn width="100">{{ item[1] }}</v-btn>
        </td>
        <td>
          <v-card width="100" style="display: inline-block; text-align: center" class="pa-1">{{ item[2] }}</v-card>
        </td>
        <td>
          {{ item[3] }}
        </td>
        <td>
          <v-card style="display: inline-block" class="pa-1 ma-1" v-for="type in item[4]">{{ type }}</v-card>
        </td>
      </tr>
    </tbody>
  </v-table>
  <v-table style="width: 600px">
    <thead>
      <tr>
        <th>Type of work</th>
        <th>Coeficient</th>
        <th>Cost of 1 row</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in coef[this.switch]">
        <td>{{ item[0] }}</td>
        <td>{{ item[1] }}</td>
        <td>{{ item[2] }}</td>
      </tr>
    </tbody>
  </v-table>
  <v-btn
    class="greenBtn"
    @click="
      () => {
        this.switch = Math.abs(this.switch - 1);
      }
    "
  >
    Edit
  </v-btn>
</template>
<script>
export default {
  data() {
    return {
      switch: 1,
      data: [
        [
          ["Pay for month", "570000"],
          ["Full cost of project", "1311000"],
          ["Cost of 1 row", "231,87"],
          ["Developers", "5"],
          ["Full time", "2.3 months"],
          ["LOC", "5654"],
        ],
        [
          ["Pay for month", "-"],
          ["Full cost of project", "-"],
          ["Cost of 1 row", "-"],
          ["Developers", "5"],
          ["Full time", "2.3 months"],
          ["LOC", "5654"],
        ],
      ],
      table: [
        [
          ["N", "60000", "375531", "3725", ["Backend", "Frontend"]],
          ["A", "150000", "83716", "1038", ["Frontend"]],
          ["O", "100000", "78151", "646", ["DevOps"]],
          ["T", "200000", "14820", "147", ["Backend", "DevOps"]],
          ["D", "60000", "4940", "98", ["Docs"]],
        ],
        [
          ["N", "-", "-", "3725", ["Backend", "Frontend"]],
          ["A", "-", "-", "1038", ["Frontend"]],
          ["O", "-", "-", "646", ["DevOps"]],
          ["T", "-", "-", "147", ["backend", "DevOps"]],
          ["D", "-", "-", "98", ["Docs"]],
        ],
      ],
      coef: [
        [
          ["Backend", 1, 231.87],
          ["Frontend", 0.8, 185.5],
          ["DevOps", 1.2, 278.25],
          ["Docs", 0.5, 115.94],
        ],
        [["-"], ["-"], ["-"], ["-"]],
      ],
    };
  },
};
</script>
