<template>
  <q-page class="q-pa-md" :class="$q.dark.isActive ? 'bg-grey-10' : 'bg-white'">
    <div class="column items-center">
      <div class="text-h6 text-center q-my-lg">Livros</div>

      <div class="row items-center justify-center q-gutter-sm q-mb-md" style="width: 100%; max-width: 1200px;">
        <q-input outlined dense debounce="300" v-model="filter" placeholder="Buscar nos registros..." style="flex: 1;">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>

        <q-btn v-if="userStore.selectedUser?.role === 'ROLE_WRITER'" label="Novo" color="primary" icon="add"
          @click="openForm" />
      </div>

      <q-card class="q-mt-md" style="width: 100%; max-width: 1200px">
        <q-table :rows="items" :columns="columns" row-key="id" separator="horizontal" flat bordered
          :table-class="$q.dark.isActive ? 'table-dark' : 'table-light'" :rows-per-page-options="[0]"
          v-model:pagination="pagination" v-model:filter="filter" :filter-method="customFilterMethod">
          <template v-slot:body-cell-actions="props" v-if="userStore.selectedUser?.role === 'ROLE_WRITER'">
            <q-td align="right">
              <q-btn icon="edit" flat dense round color="primary"
                @click="selectItemWithDialog(props.row, props.rowIndex)" />
              <q-btn icon="delete" flat dense round color="negative" @click="deleteItem(props.rowIndex)" />
            </q-td>
          </template>
        </q-table>
      </q-card>

      <q-dialog v-model="showForm" persistent>
        <q-card style="min-width: 600px; max-width: 600px">
          <q-card-section class="row items-center q-pa-sm">
            <div class="text-h6">
              {{ editingIndex !== null ? 'Editar Registro' : 'Novo Registro' }}
            </div>
            <q-space />
            <q-btn icon="close" flat round dense v-close-popup @click="cancelEditAndClose" />
          </q-card-section>
          <q-separator />

          <q-card-section>
            <div class="row q-col-gutter-md">
              <q-input v-model="form.title" label="* Título" filled :error="validate && !form.title"
                error-message="Campo obrigatório" class="col-12" />

              <q-select v-model="form.author" :options="authors" option-label="name" label="* Autor" filled
                :error="validate && !form.author" error-message="Campo obrigatório" class="col-12 col-sm-12" />

              <q-select v-model="form.genre" :options="genres" option-label="description" label="* Gênero" filled
                :error="validate && !form.genre" error-message="Campo obrigatório" class="col-12 col-sm-12" />
            </div>
          </q-card-section>

          <q-separator />

          <q-card-actions align="right">
            <q-btn label="Cancelar" flat color="primary" @click="cancelEditAndClose" />
            <q-btn :label="editingIndex !== null ? 'Salvar' : 'Incluir'" color="positive" @click="submitAndClose" />
          </q-card-actions>
        </q-card>
      </q-dialog>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useBookPage } from './BookPage';
import './BookPage.scss';
import { useUserStore } from 'src/stores/userStore';

const userStore = useUserStore();

const {
  form,
  items,
  columns,
  pagination,
  filter,
  customFilterMethod,
  validate,
  editingIndex,
  showForm,
  openForm,
  selectItemWithDialog,
  deleteItem,
  cancelEditAndClose,
  submitAndClose,
  loadItems,
  loadSelectOptions,
  authors,
  genres
} = useBookPage();

onMounted(() => {
  void loadItems();
  void loadSelectOptions();
});
</script>
