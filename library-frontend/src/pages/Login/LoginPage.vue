<template>
  <q-page class="flex flex-center bg-primary">
    <q-card class="q-pa-md shadow-2 bg-white text-primary rounded-borders" style="max-width: 600px; width: 100%">
      <q-card-section class="column items-center">
        <div ref="libraryRef" style="width: 130px; max-width: 90vw; height: auto;" />

        <div class="text-h6 text-center q-mb-sm">
          {{ isRegistering ? 'Crie sua conta' : 'Bem-vindo à Livraria' }}
        </div>
        <div class="text-subtitle2 text-center text-grey-8 q-mb-lg" v-if="!isRegistering">
          Plataforma para gestão de gêneros, autores e livros.
        </div>

        <q-input v-model="username" label="Usuário" filled class="full-width q-mb-sm" :error="submitted && !username"
          error-message="O campo Usuário é obrigatório" />

        <q-input v-model="password" :type="showPassword ? 'text' : 'password'" label="Senha" filled
          class="full-width q-mb-md" :error="submitted && !password" error-message="O campo Senha é obrigatório"
          @keyup.enter="isRegistering ? handleRegister() : handleLogin()">
          <template v-slot:append>
            <q-icon :name="showPassword ? 'visibility_off' : 'visibility'" class="cursor-pointer"
              @click="togglePasswordVisibility" />
          </template>
        </q-input>

        <q-btn :label="isRegistering ? 'REGISTRAR' : 'ACESSAR'" :color="isRegistering ? 'positive' : 'primary'"
          class="full-width q-mb-md" unelevated @click="isRegistering ? handleRegister() : handleLogin()"
          :disable="loading" :loading="loading" />

        <q-separator class="q-my-md full-width" />

        <q-btn flat color="grey-7" class="full-width"
          :label="isRegistering ? 'Já tem conta? Entrar' : 'Não tem conta? Registrar'" @click="handleOption" />

        <div class="text-caption text-center text-grey q-mt-md">
          Desenvolvido por Emanuel Bessa
        </div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup lang="ts">
import './LoginPage.scss';
import { useLoginPage } from './LoginPage';
import { onMounted, ref } from 'vue';
import lottie from 'lottie-web';

const libraryRef = ref<HTMLElement | null>(null);

onMounted(() => {
  if (libraryRef.value) {
    lottie.loadAnimation({
      container: libraryRef.value,
      renderer: 'svg',
      loop: true,
      autoplay: true,
      path: '/animations/library.json',
    });
  }
});

const {
  username,
  password,
  submitted,
  loading,
  handleLogin,
  handleRegister,
  handleOption,
  showPassword,
  togglePasswordVisibility,
  isRegistering
} = useLoginPage();
</script>
