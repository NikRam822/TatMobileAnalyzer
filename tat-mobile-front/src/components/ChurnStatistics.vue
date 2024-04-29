<template>
    <v-table density="compact">
        <thead>
            <tr>
                <th> Name </th>
                <th> Overall </th>
                <th> Churn </th>
                <th> Total </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="autor in stats">
                <td>{{ autor.name }}</td>
                <td>{{ autor.overall }}</td>
                <td>{{ autor.churn }}</td>
                <td>{{ autor.total }}</td>
            </tr>
        </tbody>
    </v-table>
</template>
<script>
import { mapState } from 'vuex'
export default {
    computed: {
        ...mapState(['stats'])
    }
}
</script>