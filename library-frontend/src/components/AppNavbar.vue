<template>
  <q-header class="bg-primary text-white custom-header">
    <q-toolbar>
      <q-btn dense flat round icon="home" aria-label="Home" to="/home" class="q-mr-sm" />
      <q-toolbar-title class="text-weight-bold">Livraria</q-toolbar-title>
      <q-space />
      <q-btn round flat>
        <div class="row items-center no-wrap">
          <q-icon name="account_circle" />
          <q-icon name="arrow_drop_down" size="16px" />
        </div>
        <q-menu>
          <q-list style="min-width: 200px; max-width: 300px">
            <q-item clickable @click="showConfigModal = true" exact class="q-dark-fix">
              <q-item-section avatar><q-icon name="settings" /></q-item-section>
              <q-item-section>Configurações</q-item-section>
            </q-item>
            <q-separator />
            <q-item clickable v-close-popup @click="logout" exact class="q-dark-fix">
              <q-item-section avatar>
                <q-icon name="logout" />
              </q-item-section>
              <q-item-section>Sair</q-item-section>
            </q-item>
          </q-list>
        </q-menu>
      </q-btn>
    </q-toolbar>
  </q-header>

  <ConfigModal v-model="showConfigModal" />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useQuasar } from 'quasar'
import { useRouter } from 'vue-router'
import ConfigModal from './ConfigModal.vue'
import { useUserStore } from 'src/stores/userStore'

const $q = useQuasar()
const showConfigModal = ref(false)

const router = useRouter()

const userStore = useUserStore()

function logout() {
  let seconds = 2
  const notif = $q.notify({
    group: false,
    timeout: 0,
    message: `Sua sessão será encerrada em ${seconds}...`,
    type: 'info',
    position: 'top-right',
    classes: 'my-notify'
  })

  const interval = setInterval(() => {
    seconds--
    if (seconds > 0) {
      notif({ message: `Sua sessão será encerrada em ${seconds}...` })
    } else {
      clearInterval(interval)
      notif()
      sessionStorage.clear()
      userStore.clear()
      void router.replace('/login')
    }
  }, 1000)
}
</script>
