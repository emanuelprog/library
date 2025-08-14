import { ref } from 'vue';
import { type QTableColumn, type QTableProps } from 'quasar';
import type { AuthorType } from 'src/types/authorType';
import { deburr } from 'lodash';
import axios from 'axios';
import { notifyConfirm, notifyError, notifySuccess, notifyWarning } from 'src/services/messageService';
import { createAuthor, listAllAuthor, removeAuthor, updateAuthor } from 'src/services/authorService';

const ALREADY_EXISTS_CODES = [409, 'ALREADY_EXISTS'];

const form = ref<AuthorType>(createEmptyForm());

const items = ref<AuthorType[]>([]);
const validate = ref(false);
const editingIndex = ref<number | null>(null);
const showForm = ref(false);

const filter = ref('');

const columns: QTableProps['columns'] = [
  {
    name: 'name',
    label: 'Nome',
    field: row => row.name,
    align: 'left',
    sortable: true
  },
  {
    name: 'createdAt',
    label: 'Data de Criação',
    field: row => row.createdAt ? new Date(row.createdAt).toLocaleDateString() : '',
    align: 'left',
    sortable: true
  },
  {
    name: 'actions',
    label: '',
    field: 'actions',
    align: 'right'
  }
];

const pagination = ref({
  page: 1,
  rowsPerPage: 10
});

function normalize(str: string): string {
  return deburr(str)
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[^a-zA-Z0-9 ]/g, '')
    .toLowerCase()
    .trim();
}

function customFilterMethod(
  rows: readonly AuthorType[],
  rawFilter: string,
  columns: readonly QTableColumn[],
): readonly AuthorType[] {
  const normalizedFilter = normalize(rawFilter);

  return rows.filter((row) => {
    return columns.some((col) => {
      const value = typeof col.field === 'function'
        ? col.field(row)
        : (row as unknown as Record<string, unknown>)[col.field];
      return normalize(String(value ?? '')).includes(normalizedFilter);
    });
  });
}

function createEmptyForm(): AuthorType {
  return {
    id: null,
    name: null,
    createdAt: null
  };
}

function clearForm() {
  form.value = createEmptyForm();
  validate.value = false;
  showForm.value = false;
}

async function loadItems() {
  const authorTypeList = await listAllAuthor();

  items.value = authorTypeList ?? [];
}

function openForm() {
  clearForm();
  editingIndex.value = null;
  showForm.value = true;
}

function selectItem(item: AuthorType, index: number) {
  editingIndex.value = index;
  validate.value = false;

  form.value = { ...item };
}

function selectItemWithDialog(item: AuthorType, index: number) {
  selectItem(item, index);
  showForm.value = true;
}

async function onSubmit() {
  validate.value = true;

  if (
    form.value.name
  ) {
    try {
      if (form.value.id !== null) {
        await updateAuthor(form.value.id, form.value);

        notifySuccess('Autor salvo com sucesso!');
      } else {
        await createAuthor(form.value);
        notifySuccess('Autor criado com sucesso!');
      }

      await loadItems();
      clearForm();
      editingIndex.value = null;
      showForm.value = false;
    } catch (error: unknown) {
      if (axios.isAxiosError(error) && error.response?.data?.message && error.response.data.code) {
        const isDuplicate = ALREADY_EXISTS_CODES.includes(error.response.data.code);
        if (isDuplicate) {
          notifyWarning(error.response.data.message);
        } else {
          notifyError(error.response.data.message);
        }
      } else {
        notifyError('Erro desconhecido ao salvar o registro.');
      }
    }
  }
}

async function submitAndClose() {
  await onSubmit();
}

async function deleteItem(index: number) {
  const confirmed = await notifyConfirm('Deseja excluir este item?')

  if (confirmed) {
    const item = items.value[index];
    if (item?.id != null) {
      await removeAuthor(item.id);
      notifySuccess('Autor excluído com sucesso!');
      await loadItems();
    }
  }
}

function cancelEditAndClose() {
  clearForm();
  editingIndex.value = null;
  showForm.value = false;
}

export function useAuthorPage() {
  return {
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
    loadItems
  };
}
