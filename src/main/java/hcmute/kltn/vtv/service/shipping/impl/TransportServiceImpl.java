package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.data.shipping.response.TransportResponse;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.entity.shipping.Transport;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.TransportStatus;
import hcmute.kltn.vtv.repository.shipping.TransportRepository;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.location.IWardService;
import hcmute.kltn.vtv.service.shipping.IDeliverService;
import hcmute.kltn.vtv.service.shipping.ITransportHandleService;
import hcmute.kltn.vtv.service.shipping.ITransportService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements ITransportService {

    private final TransportRepository transportRepository;
    private final ITransportHandleService transportHandleService;
    private final IDeliverService deliverService;
    private final IWardService wardService;
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public Transport addNewTransport(UUID orderId) {
        Transport transport = createTransport(orderId);
        try {
            return transportRepository.save(transport);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi tạo mới một đơn vận chuyển mới!");
        }
    }


    @Override
    @Transactional
    public Transport updateStatusTransport(Transport transport, String wardCode, String username,
                                           boolean handled, TransportStatus transportStatus) {
        transport.setTransportStatus(transportStatus);
        transport.setUpdateAt(LocalDateTime.now());
        try {
            transportHandleService.addNewTransportHandleByOrderId(transport.getOrderId(), wardCode, username, handled, transportStatus);
            return transportRepository.save(transport);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái đơn vận chuyển!");
        }
    }


    @Override
    public Transport updateStatusTransportByOrderId(UUID orderId, String wardCode, String username,
                                                    boolean handled, TransportStatus transportStatus) {
        Transport transport = getTransportByOrderId(orderId);
        return updateStatusTransport(transport, wardCode, username, handled, transportStatus);
    }


    @Override
    public Transport updateStatusTransportByTransportId(UUID transportId, String wardCode, String username,
                                                        boolean handled, TransportStatus transportStatus) {
        Transport transport = getTransportById(transportId);
        updateStatusTransport(transport, wardCode, username, handled, transportStatus);
        return getTransportById(transportId);
    }


    @Override
    public Transport getTransportById(UUID transportId) {
        Transport transport = transportRepository.findByTransportId(transportId)
                .orElseThrow(() -> new InternalServerErrorException("Không tìm thấy đơn vận chuyển với id: " + transportId));
        transport.setTransportHandles(transportHandleService.getAllTransportHandleByTransportId(transport.getTransportId()));
        return transport;
    }


    @Override
    public Transport getTransportByOrderId(UUID orderId) {
        Transport transport = transportRepository.findByOrderId(orderId)
                .orElseThrow(() -> new InternalServerErrorException("Không tìm thấy đơn vận chuyển theo mã đơn hàng: " + orderId));

        transport.setTransportHandles(transportHandleService.getAllTransportHandleByTransportId(transport.getTransportId()));
        return transport;
    }


    @Override
    @Transactional
    public TransportResponse updateStatusByDeliver(UUID transportId, String username, boolean handled,
                                                   TransportStatus transportStatus, String wardCode) {
       try {
           wardService.checkExistWardCode(wardCode);
           Deliver deliver = deliverService.checkTypeWorkDeliverWithTransportStatus(username, transportStatus);
           checkDeliverCanUpdateStatus(transportId, deliver);
           Transport transport = updateStatusTransportByTransportId(transportId, wardCode, username, handled, transportStatus);
           updateStatusOrderByDeliver(transport.getOrderId(), transportStatus);

           return TransportResponse.transportResponse(transport, "Dịch vụ vận chuyển đã được cập nhật trạng thái thành công!", "Success");
       }catch (Exception e) {
           throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái vận chuyển bởi nhân viên vận chuyển! " + e.getMessage());
       }
    }


    private void checkDeliverCanUpdateStatus(UUID transportId, Deliver deliver) {
        Transport transport = getTransportById(transportId);
        if (!deliver.getTransportProvider().getShortName().equals(transport.getShippingMethod())) {
            throw new BadRequestException("Nhân viên vận không cùng nhà vận chuyển với đơn hàng! Không thể cập nhật trạng thái!");
        }
        if (transport.getTransportStatus().equals(TransportStatus.CANCEL) ||
                transport.getTransportStatus().equals(TransportStatus.PENDING) ||
                transport.getTransportStatus().equals(TransportStatus.WAITING)) {
            throw new BadRequestException("Nhân viên vận không thể cập nhật trạng thái do đơn hàng đã được xử lý hoặc đã hủy!");
        }
    }


    public Transport createTransport(UUID orderId) {

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng với mã: " + orderId));
        Transport transport = new Transport();
        transport.setOrderId(orderId);
        transport.setWardCodeShop(order.getShop().getWard().getWardCode());
        transport.setWardCodeCustomer(order.getAddress().getWard().getWardCode());
        transport.setShippingMethod(order.getShippingMethod());
        transport.setTransportStatus(TransportStatus.WAITING);
        transport.setCreateAt(LocalDateTime.now());
        transport.setUpdateAt(LocalDateTime.now());

        return transport;
    }


    private void updateStatusOrderByDeliver(UUID orderId, TransportStatus transportStatus) {
       try {
           Order order = orderRepository.findByOrderId(orderId)
                   .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng với mã: " + orderId));
           order.setStatus(convertTransportStatusToOrderStatus(transportStatus));
           orderRepository.save(order);
       }catch (Exception e) {
           throw new InternalServerErrorException("Lỗi khi cập nhật trạng thái đơn hàng theo trạng thái vận chuyển của nhân viên vận chuyển!");
       }
    }


    private OrderStatus convertTransportStatusToOrderStatus(TransportStatus transportStatus) {
        return switch (transportStatus) {
            case DELIVERED -> OrderStatus.DELIVERED;
            case RETURNED -> OrderStatus.RETURNED;
            case CANCEL -> OrderStatus.CANCEL;
            case COMPLETED -> OrderStatus.COMPLETED;
            default -> OrderStatus.SHIPPING;
        };
    }

}
