package com.obsystem.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obsystem.dto.CompraDTO;
import com.obsystem.dto.DashboardDTO;
import com.obsystem.dto.TituloDTO;
import com.obsystem.dto.VendaDTO;
import com.obsystem.entities.enums.ClienteStatus;
import com.obsystem.entities.enums.FornecedorStatus;
import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;
import com.obsystem.repositories.ClienteRepository;
import com.obsystem.repositories.CompraRepository;
import com.obsystem.repositories.FornecedorRepository;
import com.obsystem.repositories.ProdutoRepository;
import com.obsystem.repositories.TituloRepository;
import com.obsystem.repositories.VendaRepository;

@Service
public class DashboardService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private TituloRepository tituloRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Transactional(readOnly = true)
    public DashboardDTO obterResumoDashboard() {
        DashboardDTO dto = new DashboardDTO();

        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        LocalDate hoje = LocalDate.now();

        // 1. Vendas
        BigDecimal totalVendas = vendaRepository.sumTotalByPeriodo(inicioMes, fimMes);
        Long qtdVendas = vendaRepository.countByPeriodo(inicioMes, fimMes);
        dto.setTotalVendasMes(totalVendas != null ? totalVendas : BigDecimal.ZERO);
        dto.setQtdVendasMes(qtdVendas != null ? qtdVendas : 0L);

        // 2. Compras
        BigDecimal totalCompras = compraRepository.sumTotalByPeriodo(inicioMes, fimMes);
        Long qtdCompras = compraRepository.countByPeriodo(inicioMes, fimMes);
        dto.setTotalComprasMes(totalCompras != null ? totalCompras : BigDecimal.ZERO);
        dto.setQtdComprasMes(qtdCompras != null ? qtdCompras : 0L);

        // 3. Financeiro
        BigDecimal totalReceber = tituloRepository.sumByTipoAndStatus(TipoTitulo.RECEITA, TituloStatus.PENDENTE);
        BigDecimal totalRecebido = tituloRepository.sumPagoByTipoAndPeriodo(TipoTitulo.RECEITA, TituloStatus.PAGO, inicioMes, fimMes);
        BigDecimal totalPagar = tituloRepository.sumByTipoAndStatus(TipoTitulo.DESPESA, TituloStatus.PENDENTE);
        BigDecimal totalPago = tituloRepository.sumPagoByTipoAndPeriodo(TipoTitulo.DESPESA, TituloStatus.PAGO, inicioMes, fimMes);
        BigDecimal totalVencido = tituloRepository.sumVencidos(hoje);
        Long qtdVencidos = tituloRepository.countVencidos(hoje);

        dto.setTotalReceberPendente(totalReceber != null ? totalReceber : BigDecimal.ZERO);
        dto.setTotalRecebidoMes(totalRecebido != null ? totalRecebido : BigDecimal.ZERO);
        dto.setTotalPagarPendente(totalPagar != null ? totalPagar : BigDecimal.ZERO);
        dto.setTotalPagoMes(totalPago != null ? totalPago : BigDecimal.ZERO);
        dto.setTotalVencidos(totalVencido != null ? totalVencido : BigDecimal.ZERO);
        dto.setQtdVencidos(qtdVencidos != null ? qtdVencidos : 0L);
        dto.setSaldoPrevisto(dto.getTotalReceberPendente().subtract(dto.getTotalPagarPendente()));

        // 4. Alertas e contagens
        Long qtdEstoqueBaixo = produtoRepository.countProdutosEstoqueBaixo();
        long qtdCliAtivos = clienteRepository.countByStatus(ClienteStatus.ATIVO);
        long qtdFornAtivos = fornecedorRepository.countByStatus(FornecedorStatus.ATIVO);

        dto.setQtdProdutosEstoqueBaixo(qtdEstoqueBaixo != null ? qtdEstoqueBaixo : 0L);
        dto.setQtdClientesAtivos(qtdCliAtivos);
        dto.setQtdFornecedoresAtivos(qtdFornAtivos);

        // 5. Listas Recentes (Top 5)
        List<VendaDTO> ultimasVendas = vendaRepository.findTop5ByOrderByEmissaoDescIdDesc()
                .stream()
                .map(VendaDTO::new)
                .collect(Collectors.toList());
        dto.setUltimasVendas(ultimasVendas);

        List<CompraDTO> ultimasCompras = compraRepository.findTop5ByOrderByEmissaoDescIdDesc()
                .stream()
                .map(CompraDTO::new)
                .collect(Collectors.toList());
        dto.setUltimasCompras(ultimasCompras);

        List<TituloDTO> proximosTitulos = tituloRepository.findTop5ByStatusOrderByVencimentoAscIdDesc(TituloStatus.PENDENTE)
                .stream()
                .map(TituloDTO::new)
                .collect(Collectors.toList());
        dto.setProximosTitulos(proximosTitulos);

        return dto;
    }
}
