import api from './api';
import type {
  CategoriaDTO,
  ClasseDTO,
  MarcaDTO,
  MedidaDTO,
  MilimetroDTO,
  ModeloDTO,
  ProdutoApoioCatalogoDTO,
  SubCategoriaDTO,
  TipoProdutoDTO,
  UnidadeMedidaDTO,
} from '../types/produtoApoio';

const BASE_URL = '/produtos-apoio';

export const produtoApoioService = {
  // Catálogo completo consolidado
  async obterCatalogo(): Promise<ProdutoApoioCatalogoDTO> {
    const response = await api.get<ProdutoApoioCatalogoDTO>(`${BASE_URL}/catalogo`);
    return response.data;
  },

  // Tipos de Produto
  async listarTiposProduto(): Promise<TipoProdutoDTO[]> {
    const response = await api.get<TipoProdutoDTO[]>(`${BASE_URL}/tipos-produto`);
    return response.data;
  },
  async salvarTipoProduto(dto: TipoProdutoDTO): Promise<TipoProdutoDTO> {
    const response = await api.post<TipoProdutoDTO>(`${BASE_URL}/tipos-produto`, dto);
    return response.data;
  },
  async excluirTipoProduto(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/tipos-produto/${id}`);
  },

  // Unidades de Medida
  async listarUnidadesMedida(): Promise<UnidadeMedidaDTO[]> {
    const response = await api.get<UnidadeMedidaDTO[]>(`${BASE_URL}/unidades-medida`);
    return response.data;
  },
  async salvarUnidadeMedida(dto: UnidadeMedidaDTO): Promise<UnidadeMedidaDTO> {
    const response = await api.post<UnidadeMedidaDTO>(`${BASE_URL}/unidades-medida`, dto);
    return response.data;
  },
  async excluirUnidadeMedida(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/unidades-medida/${id}`);
  },

  // Categorias
  async listarCategorias(): Promise<CategoriaDTO[]> {
    const response = await api.get<CategoriaDTO[]>(`${BASE_URL}/categorias`);
    return response.data;
  },
  async salvarCategoria(dto: CategoriaDTO): Promise<CategoriaDTO> {
    const response = await api.post<CategoriaDTO>(`${BASE_URL}/categorias`, dto);
    return response.data;
  },
  async excluirCategoria(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/categorias/${id}`);
  },

  // SubCategorias
  async listarSubCategorias(idCategoria?: number): Promise<SubCategoriaDTO[]> {
    const params = idCategoria ? { idCategoria } : {};
    const response = await api.get<SubCategoriaDTO[]>(`${BASE_URL}/subcategorias`, { params });
    return response.data;
  },
  async salvarSubCategoria(dto: SubCategoriaDTO): Promise<SubCategoriaDTO> {
    const response = await api.post<SubCategoriaDTO>(`${BASE_URL}/subcategorias`, dto);
    return response.data;
  },
  async excluirSubCategoria(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/subcategorias/${id}`);
  },

  // Classes
  async listarClasses(): Promise<ClasseDTO[]> {
    const response = await api.get<ClasseDTO[]>(`${BASE_URL}/classes`);
    return response.data;
  },
  async salvarClasse(dto: ClasseDTO): Promise<ClasseDTO> {
    const response = await api.post<ClasseDTO>(`${BASE_URL}/classes`, dto);
    return response.data;
  },
  async excluirClasse(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/classes/${id}`);
  },

  // Modelos
  async listarModelos(): Promise<ModeloDTO[]> {
    const response = await api.get<ModeloDTO[]>(`${BASE_URL}/modelos`);
    return response.data;
  },
  async salvarModelo(dto: ModeloDTO): Promise<ModeloDTO> {
    const response = await api.post<ModeloDTO>(`${BASE_URL}/modelos`, dto);
    return response.data;
  },
  async excluirModelo(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/modelos/${id}`);
  },

  // Marcas
  async listarMarcas(): Promise<MarcaDTO[]> {
    const response = await api.get<MarcaDTO[]>(`${BASE_URL}/marcas`);
    return response.data;
  },
  async salvarMarca(dto: MarcaDTO): Promise<MarcaDTO> {
    const response = await api.post<MarcaDTO>(`${BASE_URL}/marcas`, dto);
    return response.data;
  },
  async excluirMarca(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/marcas/${id}`);
  },

  // Milímetros
  async listarMilimetros(): Promise<MilimetroDTO[]> {
    const response = await api.get<MilimetroDTO[]>(`${BASE_URL}/milimetros`);
    return response.data;
  },
  async salvarMilimetro(dto: MilimetroDTO): Promise<MilimetroDTO> {
    const response = await api.post<MilimetroDTO>(`${BASE_URL}/milimetros`, dto);
    return response.data;
  },
  async excluirMilimetro(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/milimetros/${id}`);
  },

  // Medidas
  async listarMedidas(): Promise<MedidaDTO[]> {
    const response = await api.get<MedidaDTO[]>(`${BASE_URL}/medidas`);
    return response.data;
  },
  async salvarMedida(dto: MedidaDTO): Promise<MedidaDTO> {
    const response = await api.post<MedidaDTO>(`${BASE_URL}/medidas`, dto);
    return response.data;
  },
  async excluirMedida(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/medidas/${id}`);
  },
};

export default produtoApoioService;
